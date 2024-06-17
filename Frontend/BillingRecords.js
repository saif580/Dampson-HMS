import React, { useEffect, useState } from "react";
import emailjs from "emailjs-com";
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Modal from "./Modal";
import "./BillingRecords.css";

const BillingRecords = () => {
  const [bills, setBills] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newBill, setNewBill] = useState({
    clinicId: "",
    patientId: "",
    patientFirstName: "",
    patientLastName: "",
    patientEmail: "",
    amount: "",
    paymentMethod: "",
  });
  const [loading, setLoading] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [lazyLoadCount, setLazyLoadCount] = useState(9);
  const [currentBillSending, setCurrentBillSending] = useState(null);

  useEffect(() => {
    const fetchBills = async () => {
      setLoading(true);
      try {
        const response = await fetch("http://localhost:7010/billings", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Error fetching bills: ${response.statusText}`);
        }

        const data = await response.json();
        setBills(data);
      } catch (error) {
        console.error("Error fetching bills:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchBills();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewBill((prevBill) => ({
      ...prevBill,
      [name]: value,
    }));
  };

  const handleAddBill = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch("http://localhost:7010/billings/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(newBill),
      });

      setLoading(false);

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error adding bill: ${errorText}`);
      }

      const data = await response.json();
      setBills((prevBills) => [...prevBills, data]);
      setIsModalOpen(false);
      setNewBill({
        clinicId: "",
        patientId: "",
        patientFirstName: "",
        patientLastName: "",
        patientEmail: "",
        amount: "",
        paymentMethod: "",
      });
      toast.success("Bill added successfully!");
    } catch (error) {
      setLoading(false);
      console.error("Error adding bill:", error);
      toast.error(`${error.message}`);
    }
  };

  const loadMoreBills = () => {
    setLazyLoadCount((prevCount) => prevCount + 9);
  };

  const handleGeneratePdf = async (billingId) => {
    try {
      const response = await fetch(
        `http://localhost:7010/billings/${billingId}/generatePdf`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(`Error generating PDF: ${response.statusText}`);
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `bill_${billingId}.pdf`;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error generating PDF:", error);
      toast.error(`Error generating PDF: ${error.message}`);
    }
  };

  // Function to handle email sending
  const sendEmailNotification = (bill) => {
    setCurrentBillSending(bill.billingId);
    const serviceID = process.env.REACT_APP_EMAILJS_SERVICE_ID;
    const templateID = process.env.REACT_APP_EMAILJS_TEMPLATE_ID;
    const userID = process.env.REACT_APP_EMAILJS_USER_ID;

    const emailParams = {
      to_name: `${bill.patientFirstName} ${bill.patientLastName}`,
      to_email: bill.patientEmail,
      message: `Your billing payment is successful. 
      Amount: ${bill.amount}`,
    };

    emailjs
      .send(serviceID, templateID, emailParams, userID)
      .then((response) => {
        console.log("Email sent successfully:", response.status, response.text);
        toast.success("Notification sent successfully!");
      })
      .catch((error) => {
        console.error("Error sending email:", error);
        toast.error(`Error sending email: ${error.message}`);
      })
      .finally(() => {
        setCurrentBillSending(null);
      });
  };

  const filteredBills = bills
    .filter(
      (bill) =>
        `${bill.patientId}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
        `${bill.billingId}`.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .slice(0, lazyLoadCount);

  return (
    <div className="billing-records">
      <h2>Billing Records</h2>
      <input
        type="text"
        placeholder="Search by Patient ID or Billing ID..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-bar"
      />
      <button className="btn" onClick={() => setIsModalOpen(true)}>
        Add Bill
      </button>

      {loading ? (
        <div className="spinner">
          <ClipLoader size={50} color={"#123abc"} loading={loading} />
        </div>
      ) : (
        <>
          <table className="billing-table">
            <thead>
              <tr>
                <th>Billing ID</th>
                <th>Clinic ID</th>
                <th>Patient ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Amount</th>
                <th>Payment Method</th>
                <th>Generate PDF</th>
                <th>Send Notification</th>
              </tr>
            </thead>
            <tbody>
              {filteredBills.map((bill) => (
                <tr key={bill.billingId}>
                  <td>{bill.billingId}</td>
                  <td>{bill.clinicId}</td>
                  <td>{bill.patientId}</td>
                  <td>{bill.patientFirstName}</td>
                  <td>{bill.patientLastName}</td>
                  <td>{bill.patientEmail}</td>
                  <td>{bill.amount}</td>
                  <td>{bill.paymentMethod}</td>
                  <td>
                    <button
                      className="pdf-button"
                      onClick={() => handleGeneratePdf(bill.billingId)}
                    >
                      Generate
                    </button>
                  </td>
                  <td>
                    <button
                      className="notification-button"
                      onClick={() => sendEmailNotification(bill)}
                      disabled={currentBillSending === bill.billingId}
                    >
                      {currentBillSending === bill.billingId ? (
                        <ClipLoader size={15} color={"#fff"} />
                      ) : (
                        "Share"
                      )}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {filteredBills.length < bills.length && (
            <button className="load-more-button" onClick={loadMoreBills}>
              Load More
            </button>
          )}
        </>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        className="billing-modal"
      >
        <h2 className="modal__header">Add New Bill</h2>
        <form className="modal__form" onSubmit={handleAddBill}>
          <label>Clinic ID</label>
          <input
            type="text"
            name="clinicId"
            value={newBill.clinicId}
            onChange={handleInputChange}
            required
          />
          <label>Patient ID</label>
          <input
            type="text"
            name="patientId"
            value={newBill.patientId}
            onChange={handleInputChange}
            required
          />
          <label>First Name</label>
          <input
            type="text"
            name="patientFirstName"
            value={newBill.patientFirstName}
            onChange={handleInputChange}
            required
          />
          <label>Last Name</label>
          <input
            type="text"
            name="patientLastName"
            value={newBill.patientLastName}
            onChange={handleInputChange}
            required
          />
          <label>Email</label>
          <input
            type="email"
            name="patientEmail"
            value={newBill.patientEmail}
            onChange={handleInputChange}
            required
          />
          <label>Amount</label>
          <input
            type="text"
            name="amount"
            value={newBill.amount}
            onChange={handleInputChange}
            required
          />
          <label>Payment Method</label>
          <input
            type="text"
            name="paymentMethod"
            value={newBill.paymentMethod}
            onChange={handleInputChange}
            required
          />
          <button className="btn" type="submit" disabled={loading}>
            {loading ? <ClipLoader size={20} color={"#fff"} /> : "Add Bill"}
          </button>
        </form>
      </Modal>

      <ToastContainer />
    </div>
  );
};

export default BillingRecords;
