import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./Clinic.css";
import Modal from "./Modal";

const Clinic = () => {
  const { doctorId } = useParams();
  const [clinic, setClinic] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalLoading, setModalLoading] = useState(false);
  const [newClinic, setNewClinic] = useState({
    doctorName: "",
    clinicName: "",
    clinicTime: "",
    maxPatientsPerHour: "",
    address: "",
    contactNumber: "",
    email: "",
    clinicSpeciality: "",
    clinicFacilities: "",
    operatingDays: "",
    appointmentDuration: "",
    doctorFees: "",
  });

  useEffect(() => {
    const fetchClinic = async () => {
      setLoading(true);
      try {
        const response = await fetch(`http://localhost:7010/api/clinics/doctor/1`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Error fetching clinic: ${response.statusText}`);
        }

        const data = await response.json();
        const clinicData = data[0] || null;
        setClinic(clinicData);
        if (clinicData) {
          setNewClinic({
            doctorName: clinicData.doctorName,
            clinicName: clinicData.clinicName,
            clinicTime: clinicData.clinicTime,
            maxPatientsPerHour: clinicData.maxPatientsPerHour,
            address: clinicData.address,
            contactNumber: clinicData.contactNumber,
            email: clinicData.email,
            clinicSpeciality: clinicData.clinicSpeciality,
            clinicFacilities: clinicData.clinicFacilities,
            operatingDays: clinicData.operatingDays,
            appointmentDuration: clinicData.appointmentDuration,
            doctorFees: clinicData.doctorFees,
          });
          toast.success("Clinic data fetched successfully!");
        }
      } catch (error) {
        console.error("Error fetching clinic:", error);
        toast.error(`Error fetching clinic: ${error.message}`);
      } finally {
        setLoading(false);
      }
    };

    fetchClinic();
  }, [doctorId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
  
    // Enforce contact number to be only digits
    if (name === "contactNumber") {
      const sanitizedValue = value.replace(/\D/g, ""); // Remove non-digit characters
      if (sanitizedValue.length <= 10) {
        setNewClinic((prevClinic) => ({
          ...prevClinic,
          [name]: sanitizedValue,
        }));
      }
    } else {
      setNewClinic((prevClinic) => ({
        ...prevClinic,
        [name]: value,
      }));
    }
  };

  const handleAddOrEditClinic = async (e) => {
    e.preventDefault();
    if (newClinic.contactNumber.length !== 10) {
      toast.error("Phone number should be of 10 digits");
      return;
    }
    setModalLoading(true);
    try {
      const body = { ...newClinic, clinicId: 1 };
      const response = await fetch("http://localhost:7010/api/clinics", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(body),
      });
  
      setModalLoading(false);
  
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error ${clinic ? "editing" : "adding"} clinic: ${errorText}`);
      }
  
      const data = await response.json();
      setClinic(data);
      setIsModalOpen(false);
      toast.success(`Clinic ${clinic ? "updated" : "added"} successfully!`);
    } catch (error) {
      setModalLoading(false);
      console.error(`Error ${clinic ? "editing" : "adding"} clinic:`, error);
      toast.error(`Error ${clinic ? "editing" : "adding"} clinic: ${error.message}`);
    }
  };

  return (
    <div className="clinic">
      <h2>Clinic Information</h2>
      <button className="btn" onClick={() => setIsModalOpen(true)}>
        {clinic ? "Edit Clinic" : "Add Clinic"}
      </button>
      {loading ? (
        <div className="spinner">
          <ClipLoader size={50} color={"#123abc"} loading={loading} />
        </div>
      ) : (
        clinic && (
          <div className="clinic-details">
            <p><strong>Doctor Name:</strong> {clinic.doctorName}</p>
            <p><strong>Clinic Name:</strong> {clinic.clinicName}</p>
            <p><strong>Clinic Time:</strong> {clinic.clinicTime}</p>
            <p><strong>Max Patients Per Hour:</strong> {clinic.maxPatientsPerHour}</p>
            <p><strong>Address:</strong> {clinic.address}</p>
            <p><strong>Contact Number:</strong> {clinic.contactNumber}</p>
            <p><strong>Email:</strong> {clinic.email}</p>
            <p><strong>Speciality:</strong> {clinic.clinicSpeciality}</p>
            <p><strong>Facilities:</strong> {clinic.clinicFacilities}</p>
            <p><strong>Operating Days:</strong> {clinic.operatingDays}</p>
            <p><strong>Appointment Duration:</strong> {clinic.appointmentDuration} minutes</p>
            <p><strong>Doctor Fees:</strong> ₹{clinic.doctorFees}</p>
          </div>
        )
      )}

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} className="clinic-modal">
        <div className="modal__content">
          <h2 className="modal__header">{clinic ? "Edit Clinic" : "Add New Clinic"}</h2>
          <form className="modal__form" onSubmit={handleAddOrEditClinic}>
            <label>Doctor Name</label>
            <input
              type="text"
              name="doctorName"
              value={newClinic.doctorName}
              onChange={handleInputChange}
              required
            />
            <label>Clinic Name</label>
            <input
              type="text"
              name="clinicName"
              value={newClinic.clinicName}
              onChange={handleInputChange}
              required
            />
            <label>Clinic Time</label>
            <input
              type="text"
              name="clinicTime"
              value={newClinic.clinicTime}
              onChange={handleInputChange}
              required
            />
            <label>Max Patients Per Hour</label>
            <input
              type="number"
              name="maxPatientsPerHour"
              value={newClinic.maxPatientsPerHour}
              onChange={handleInputChange}
              required
            />
            <label>Address</label>
            <input
              type="text"
              name="address"
              value={newClinic.address}
              onChange={handleInputChange}
              required
            />
            <label>Contact Number</label>
            <input
              type="text"
              name="contactNumber"
              value={newClinic.contactNumber}
              onChange={handleInputChange}
              required
            />
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={newClinic.email}
              onChange={handleInputChange}
              required
            />
            <label>Clinic Speciality</label>
            <input
              type="text"
              name="clinicSpeciality"
              value={newClinic.clinicSpeciality}
              onChange={handleInputChange}
              required
            />
            <label>Clinic Facilities</label>
            <input
              type="text"
              name="clinicFacilities"
              value={newClinic.clinicFacilities}
              onChange={handleInputChange}
              required
            />
            <label>Operating Days</label>
            <input
              type="text"
              name="operatingDays"
              value={newClinic.operatingDays}
              onChange={handleInputChange}
              required
            />
            <label>Appointment Duration</label>
            <input
              type="number"
              name="appointmentDuration"
              value={newClinic.appointmentDuration}
              onChange={handleInputChange}
              required
            />
            <label>Doctor Fees</label>
            <input
              type="number"
              name="doctorFees"
              value={newClinic.doctorFees}
              onChange={handleInputChange}
              required
            />
            <button className="btn" type="submit" disabled={modalLoading}>
              {modalLoading ? <ClipLoader size={20} color={"#fff"} /> : clinic ? "Update Clinic" : "Add Clinic"}
            </button>
          </form>
        </div>
      </Modal>

      <ToastContainer />
    </div>
  );
};

export default Clinic;
