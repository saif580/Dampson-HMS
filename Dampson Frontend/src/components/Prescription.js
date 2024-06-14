import React, { useEffect, useState } from "react";
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./Prescription.css";

const Prescription = () => {
  const [prescriptions, setPrescriptions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchPrescriptions = async () => {
      setLoading(true);
      try {
        const response = await fetch("http://localhost:7010/prescriptions", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Error fetching prescriptions: ${response.statusText}`);
        }

        const data = await response.json();
        setPrescriptions(data);
      } catch (error) {
        console.error("Error fetching prescriptions:", error);
        toast.error(`Error fetching prescriptions: ${error.message}`);
      } finally {
        setLoading(false);
      }
    };

    fetchPrescriptions();
  }, []);

  return (
    <div className="prescription">
      <h2>Prescriptions</h2>
      {loading ? (
        <div className="spinner">
          <ClipLoader size={50} color={"#123abc"} loading={loading} />
        </div>
      ) : (
        <table className="prescription-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Patient Name</th>
              <th>Doctor Name</th>
              <th>Medication</th>
              <th>Dosage</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {prescriptions.map((prescription) => (
              <tr key={prescription.id}>
                <td>{prescription.id}</td>
                <td>{prescription.patientName}</td>
                <td>{prescription.doctorName}</td>
                <td>{prescription.medication}</td>
                <td>{prescription.dosage}</td>
                <td>{new Date(prescription.date).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <ToastContainer />
    </div>
  );
};

export default Prescription;
