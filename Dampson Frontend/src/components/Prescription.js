import emailjs from "emailjs-com";
import { PDFDocument, StandardFonts, rgb } from "pdf-lib";
import React, { useEffect, useState } from "react";
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Modal from "./Modal";
import "./Prescription.css";

const Prescription = () => {
  const [prescriptions, setPrescriptions] = useState([]);
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isPrevModalOpen, setIsPrevModalOpen] = useState(false);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [selectedPrescriptions, setSelectedPrescriptions] = useState([]);
  const [newPrescription, setNewPrescription] = useState({
    diagnosis: "",
    date: new Date().toISOString().split("T")[0], // Set default date to today's date
    tests: "None",
    prescriptionDetails: [{ medicine: "", dosage: "", instructions: "" }],
  });
  const [file, setFile] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [lazyLoadCount, setLazyLoadCount] = useState(8);

  useEffect(() => {
    const fetchPatients = async () => {
      setLoading(true);
      try {
        const response = await fetch("http://localhost:7010/patients", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Error fetching patients: ${response.statusText}`);
        }

        const data = await response.json();
        data.sort((a, b) => b.patientId - a.patientId);
        setPatients(data);
        toast.success("Patients fetched successfully!")
      } catch (error) {
        console.error("Error fetching patients:", error);
        toast.error(`Error fetching patients: ${error.message}`);
      } finally {
        setLoading(false);
      }
    };

    fetchPatients();
  }, []);

  const handleInputChange = (e, index) => {
    const { name, value } = e.target;
    const prescriptionDetails = [...newPrescription.prescriptionDetails];
    prescriptionDetails[index][name] = value;
    setNewPrescription((prevPrescription) => ({
      ...prevPrescription,
      prescriptionDetails,
    }));
  };

  const handleAddRow = () => {
    setNewPrescription((prevPrescription) => ({
      ...prevPrescription,
      prescriptionDetails: [
        ...prevPrescription.prescriptionDetails,
        { medicine: "", dosage: "", instructions: "" },
      ],
    }));
  };

  const handleRemoveRow = (index) => {
    const prescriptionDetails = [...newPrescription.prescriptionDetails];
    prescriptionDetails.splice(index, 1);
    setNewPrescription((prevPrescription) => ({
      ...prevPrescription,
      prescriptionDetails,
    }));
  };

  const handleGeneralInputChange = (e) => {
    const { name, value } = e.target;
    setNewPrescription((prevPrescription) => ({
      ...prevPrescription,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleAddPrescription = async (e) => {
    e.preventDefault();
    setLoading(true);

    const formData = new FormData();
    const prescriptionData = {
      ...newPrescription,
      patientId: selectedPatient,
      clinicId: 1,
    };

    formData.append("medicalRecord", JSON.stringify(prescriptionData));
    if (file) {
      formData.append("file", file);
    }

    console.log("FormData before sending:", Array.from(formData.entries()));

    try {
      const response = await fetch(`http://localhost:7010/medicalrecords`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: formData,
      });

      setLoading(false);

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error adding prescription: ${errorText}`);
      }

      const data = await response.json();
      setPrescriptions((prevPrescriptions) => [data, ...prevPrescriptions]); // Add new prescription to the top
      setIsModalOpen(false);
      setNewPrescription({
        diagnosis: "",
        date: new Date().toISOString().split("T")[0],
        tests: "None",
        prescriptionDetails: [{ medicine: "", dosage: "", instructions: "" }],
      });
      setFile(null);
      toast.success("Prescription added successfully!");
    } catch (error) {
      setLoading(false);
      console.error("Error adding prescription:", error);
      toast.error(`${error.message}`);
    }
  };

  const handleSendNotification = async (patientId, recordId) => {
    try {
      setLoading(true);
      // Fetch patient details
      const patientResponse = await fetch(
        `http://localhost:7010/patients/id/${patientId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!patientResponse.ok) {
        throw new Error(
          `Error fetching patient details: ${patientResponse.statusText}`
        );
      }

      const patient = await patientResponse.json();
      console.log(patient);

      // Fetch clinic details
      const clinicResponse = await fetch(
        `http://localhost:7010/api/clinics/doctor/1`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!clinicResponse.ok) {
        throw new Error(
          `Error fetching clinic details: ${clinicResponse.statusText}`
        );
      }

      const clinic = await clinicResponse.json();
      console.log(clinic);

      // Fetch specific medical record
      const recordResponse = await fetch(
        `http://localhost:7010/medicalrecords/${recordId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!recordResponse.ok) {
        throw new Error(
          `Error fetching medical record: ${recordResponse.statusText}`
        );
      }

      const record = await recordResponse.json();
      console.log(record);
      // Send the PDF via EmailJS

      const serviceID = process.env.REACT_APP_EMAILJS_SERVICE_ID;
      const templateID = process.env.REACT_APP_EMAILJS_TEMPLATE_ID;
      const userID = process.env.REACT_APP_EMAILJS_USER_ID;
      const generatePrescriptionDetailsHTML = (details) => {
        return details
          .map(
            (detail) => `
          <tr>
            <td style="border: 1px solid #d0d0d0; padding: 8px;">${detail.medicine}</td>
            <td style="border: 1px solid #d0d0d0; padding: 8px;">${detail.dosage}</td>
            <td style="border: 1px solid #d0d0d0; padding: 8px;">${detail.instructions}</td>
          </tr>
        `
          )
          .join("");
      };

      const emailParams = {
        to_name: `${patient.firstName} ${patient.lastName}`,
        clinicName: clinic[0].clinicName,
        to_email: patient.email,
        // pdf_base64: pdfBase64,
        patient_id: patient.patientId,
        patient_phone: patient.phone,
        prescription_details_html: generatePrescriptionDetailsHTML(
          record.prescriptionDetails
        ),
        additional_notes: "Please follow the instructions carefully.",
      };

      console.log("Email Params:", emailParams);

      emailjs.send(serviceID, templateID, emailParams, userID).then(
        (response) => {
          console.log(
            "Email sent successfully:",
            response.status,
            response.text
          );
          toast.success("Notification sent successfully!");
        },
        (error) => {
          console.error("Error sending email:", error);
          toast.error(`Error sending email: ${error.message}`);
        }
      );
    } catch (error) {
      console.error("Error Generaing PDF" + error);
      toast.error(`Error generating PDF: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handlePrint = async (patientId, recordId) => {
    try {
      setLoading(true);
      // Fetch patient details
      const patientResponse = await fetch(
        `http://localhost:7010/patients/id/${patientId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!patientResponse.ok) {
        throw new Error(
          `Error fetching patient details: ${patientResponse.statusText}`
        );
      }

      const patient = await patientResponse.json();
      console.log(patient);

      // Calculate patient's age
      const birthDate = new Date(patient.dateOfBirth);
      const ageDifMs = Date.now() - birthDate.getTime();
      const ageDate = new Date(ageDifMs);
      const age = Math.abs(ageDate.getUTCFullYear() - 1970);

      // Fetch clinic details
      const clinicResponse = await fetch(
        `http://localhost:7010/api/clinics/doctor/1`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!clinicResponse.ok) {
        throw new Error(
          `Error fetching clinic details: ${clinicResponse.statusText}`
        );
      }

      const clinic = await clinicResponse.json();
      console.log(clinic);

      // Fetch specific medical record
      const recordResponse = await fetch(
        `http://localhost:7010/medicalrecords/${recordId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!recordResponse.ok) {
        throw new Error(
          `Error fetching medical record: ${recordResponse.statusText}`
        );
      }

      const record = await recordResponse.json();
      console.log(record);

      // Create a new PDF document
      const pdfDoc = await PDFDocument.create();
      const timesRomanFont = await pdfDoc.embedFont(StandardFonts.TimesRoman);
      const boldFont = await pdfDoc.embedFont(StandardFonts.TimesRomanBold);

      // Add a page to the PDF document
      const page = pdfDoc.addPage([600, 800]);

      // Embed logo image
      const logoImageBytes = await fetch("/img/icon-1.png").then((res) =>
        res.arrayBuffer()
      );
      const logoImage = await pdfDoc.embedPng(logoImageBytes);
      const logoDims = logoImage.scale(0.4);

      // Draw logo image
      page.drawImage(logoImage, {
        x: -100,
        y: 710,
        width: logoDims.width,
        height: logoDims.height,
      });

      // Doctor and Clinic Info
      const clinicDetailsStartY = 770;
      page.drawText("Dr. " + (clinic[0].doctorName || ""), {
        x: 240,
        y: clinicDetailsStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText(clinic[0].clinicSpeciality || "", {
        x: 240,
        y: clinicDetailsStartY - 20,
        size: 12,
        font: timesRomanFont,
      });
      page.drawText(clinic[0].clinicFacilities || "", {
        x: 240,
        y: clinicDetailsStartY - 40,
        size: 12,
        font: timesRomanFont,
      });

      page.drawText(clinic[0].clinicName || "", {
        x: 400,
        y: clinicDetailsStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText(clinic[0].address || "", {
        x: 400,
        y: clinicDetailsStartY - 20,
        size: 12,
        font: timesRomanFont,
      });
      page.drawText(`Ph: ${clinic[0].contactNumber || ""}`, {
        x: 400,
        y: clinicDetailsStartY - 40,
        size: 12,
        font: timesRomanFont,
      });

      // Draw horizontal line
      page.drawLine({
        start: { x: 20, y: clinicDetailsStartY - 50 },
        end: { x: 580, y: clinicDetailsStartY - 50 },
        thickness: 1,
        color: rgb(0, 0, 0),
      });
      page.drawLine({
        start: { x: 20, y: clinicDetailsStartY - 55 },
        end: { x: 580, y: clinicDetailsStartY - 55 },
        thickness: 1,
        color: rgb(0, 0, 0),
      });

      // Patient Details
      const patientDetailsStartY = clinicDetailsStartY - 70;
      page.drawText(
        `Patient ID: ${patient.patientId}    Name: ${patient.firstName || ""} ${
          patient.lastName || ""
        }`,
        {
          x: 20,
          y: patientDetailsStartY,
          size: 12,
          font: boldFont,
        }
      );
      page.drawText(`Age: ${age}    Phone: ${patient.phone || ""}`, {
        x: 20,
        y: patientDetailsStartY - 20,
        size: 12,
        font: timesRomanFont,
      });
      page.drawLine({
        start: { x: 20, y: clinicDetailsStartY - 103 },
        end: { x: 580, y: clinicDetailsStartY - 103 },
        thickness: 1,
        color: rgb(0, 0, 0),
      });

      // Diagnosis
      const diagnosisStartY = patientDetailsStartY - 50;
      page.drawText("Diagnosis", {
        x: 20,
        y: diagnosisStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText(record.diagnosis || "", {
        x: 20,
        y: diagnosisStartY - 20,
        size: 12,
        font: timesRomanFont,
      });

      // Prescription Details
      const prescriptionStartY = diagnosisStartY - 40;
      page.drawText("Medicine", {
        x: 20,
        y: prescriptionStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText("Dosage", {
        x: 200,
        y: prescriptionStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText("Instructions", {
        x: 300,
        y: prescriptionStartY,
        size: 12,
        font: boldFont,
      });

      let currentY = prescriptionStartY - 20;
      record.prescriptionDetails.forEach((detail, index) => {
        page.drawText(`${index + 1}) ${detail.medicine || ""}`, {
          x: 20,
          y: currentY,
          size: 12,
          font: timesRomanFont,
        });
        page.drawText(`${detail.dosage || ""}`, {
          x: 200,
          y: currentY,
          size: 12,
          font: timesRomanFont,
        });
        page.drawText(`${detail.instructions || ""}`, {
          x: 300,
          y: currentY,
          size: 12,
          font: timesRomanFont,
        });
        currentY -= 20;
      });

      // Tests
      const testsStartY = currentY - 40;
      page.drawText("Tests", {
        x: 20,
        y: testsStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText(record.tests || "", {
        x: 20,
        y: testsStartY - 20,
        size: 12,
        font: timesRomanFont,
      });

      // Clinic Details at bottom
      const clinicBottomStartY = 70; // Adjust this value to set the distance from the bottom of the page

      // Draw the line just above clinic details
      page.drawLine({
        start: { x: 20, y: clinicBottomStartY + 30 },
        end: { x: 580, y: clinicBottomStartY + 30 },
        thickness: 1,
        color: rgb(0, 0, 0),
      });

      page.drawText(clinic[0].clinicName || "", {
        x: 20,
        y: clinicBottomStartY,
        size: 12,
        font: boldFont,
      });
      page.drawText(clinic[0].address || "", {
        x: 20,
        y: clinicBottomStartY - 20,
        size: 12,
        font: timesRomanFont,
      });
      page.drawText(`For Appointment Call: ${clinic[0].contactNumber || ""}`, {
        x: 20,
        y: clinicBottomStartY - 40,
        size: 12,
        font: timesRomanFont,
      });
      page.drawText(`Find us on: ${clinic[0].email || ""}`, {
        x: 20,
        y: clinicBottomStartY - 60,
        size: 12,
        font: timesRomanFont,
      });

      // Serialize the PDF document to bytes (a Uint8Array)
      const pdfBytes = await pdfDoc.save();

      // Create a Blob from the PDF bytes and generate a URL
      const pdfBlob = new Blob([pdfBytes], { type: "application/pdf" });
      const url = URL.createObjectURL(pdfBlob);

      // Open the PDF in a new tab
      window.open(url, "_blank");

      toast.success("PDF generated successfully!");
    } catch (error) {
      console.error("Error generating PDF:", error);
      toast.error(`Error generating PDF: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleViewPrescription = async (patientId) => {
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:7010/medicalrecords/patient/${patientId}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(`Error fetching prescriptions: ${response.statusText}`);
      }

      let data = await response.json();
      data = data.sort((a, b) => b.recordId - a.recordId);

      setSelectedPrescriptions(data);
      setIsPrevModalOpen(true);
      toast.success("Prescriptions fetched successfully!");
    } catch (error) {
      console.error("Error fetching prescriptions:", error);
      toast.error(`Error fetching prescriptions: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const loadMorePrescriptions = () => {
    setLazyLoadCount((prevCount) => prevCount + 8);
  };

  const filteredPatients = patients.filter(
    (patient) =>
      patient.patientId.toString().includes(searchTerm.toLowerCase()) ||
      `${patient.firstName} ${patient.lastName}`
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
  );

  return (
    <div className="prescription">
      <h2>Prescriptions</h2>
      <input
        type="text"
        placeholder="Search by patient ID or name..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-bar"
      />
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
              <th>New Prescription</th>
              <th>Previous Prescriptions</th>
            </tr>
          </thead>
          <tbody>
            {filteredPatients.map((patient) => (
              <tr key={patient.patientId}>
                <td>{patient.patientId}</td>
                <td>{`${patient.firstName} ${patient.lastName}`}</td>
                <td>
                  <button
                    className="btn"
                    onClick={() => {
                      setSelectedPatient(patient.patientId);
                      setIsModalOpen(true);
                    }}
                  >
                    New Prescription
                  </button>
                </td>
                <td>
                  <button
                    className="btn"
                    onClick={() => handleViewPrescription(patient.patientId)}
                  >
                    All Prescriptions
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        className="prescription-modal"
      >
        <h2 className="modal__header">Add New Prescription</h2>
        <form className="modal__form" onSubmit={handleAddPrescription}>
          <input
            type="hidden"
            name="patientId"
            value={selectedPatient || ""}
            onChange={handleGeneralInputChange}
          />
          <label>Diagnosis</label>
          <input
            type="text"
            name="diagnosis"
            value={newPrescription.diagnosis}
            onChange={handleGeneralInputChange}
            required
          />
          <label>Date</label>
          <input
            type="date"
            name="date"
            value={newPrescription.date}
            onChange={handleGeneralInputChange}
            required
          />
          <label>Tests</label>
          <input
            type="text"
            name="tests"
            value={newPrescription.tests}
            onChange={handleGeneralInputChange}
          />
          {newPrescription.prescriptionDetails.map((detail, index) => (
            <div key={index} className="prescription-detail-row">
              <input
                type="text"
                name="medicine"
                placeholder="Medicine"
                value={detail.medicine}
                onChange={(e) => handleInputChange(e, index)}
                required
              />
              <input
                type="text"
                name="dosage"
                placeholder="Dosage"
                value={detail.dosage}
                onChange={(e) => handleInputChange(e, index)}
                required
              />
              <input
                type="text"
                name="instructions"
                placeholder="Duration/Instructions"
                value={detail.instructions}
                onChange={(e) => handleInputChange(e, index)}
                required
              />
              {index > 0 && (
                <button
                  type="button"
                  className="btn remove-btn"
                  onClick={() => handleRemoveRow(index)}
                >
                  Remove
                </button>
              )}
            </div>
          ))}
          <button type="button" className="btn add-btn" onClick={handleAddRow}>
            Add More
          </button>
          <label>Upload File</label>
          <input type="file" name="file" onChange={handleFileChange} />
          <button className="btn" type="submit" disabled={loading}>
            {loading ? (
              <ClipLoader size={20} color={"#fff"} />
            ) : (
              "Add Prescription"
            )}
          </button>
        </form>
      </Modal>

      <Modal
        isOpen={isPrevModalOpen}
        onClose={() => setIsPrevModalOpen(false)}
        className="prescription-modal"
      >
        <h2 className="modal__header">All Prescriptions</h2>
        {selectedPrescriptions.length > 0 ? (
          <>
            <ul className="prescription-list">
              {selectedPrescriptions
                .slice(0, lazyLoadCount)
                .map((prescription) => (
                  <li key={prescription.recordId} className="prescription-item">
                    <span>
                      <strong>Record ID:</strong> {prescription.recordId}
                    </span>
                    <span>
                      <strong>Date:</strong>{" "}
                      {new Date(prescription.date).toLocaleDateString()}
                    </span>
                    <div className="btn-container">
                      <button
                        className="btn"
                        onClick={() =>
                          window.open(prescription.images, "_blank")
                        }
                        disabled={
                          !prescription.images ||
                          prescription.images.includes("null")
                        }
                        style={{
                          backgroundColor:
                            !prescription.images ||
                            prescription.images.includes("null")
                              ? "gray"
                              : "",
                          cursor:
                            !prescription.images ||
                            prescription.images.includes("null")
                              ? "not-allowed"
                              : "pointer",
                        }}
                      >
                        View Image
                      </button>
                      <button
                        className="btn"
                        onClick={() =>
                          handleSendNotification(
                            prescription.patientId,
                            prescription.recordId
                          )
                        }
                      >
                        Send Notification
                      </button>
                      <button
                        className="btn"
                        onClick={() =>
                          handlePrint(
                            prescription.patientId,
                            prescription.recordId
                          )
                        }
                      >
                        Print
                      </button>
                    </div>
                  </li>
                ))}
            </ul>
            {lazyLoadCount < selectedPrescriptions.length && (
              <button
                className="btn load-more-btn"
                onClick={loadMorePrescriptions}
              >
                Load More
              </button>
            )}
          </>
        ) : (
          <p>No prescriptions found.</p>
        )}
      </Modal>

      <ToastContainer />
    </div>
  );
};

export default Prescription;
