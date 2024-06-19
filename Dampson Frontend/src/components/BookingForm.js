import React, { useState } from "react";
import { ClipLoader } from "react-spinners";
import { toast } from "react-toastify";

const BookingForm = ({ onSubmit, onClose }) => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("09:00");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const appointment = {
      name,
      email,
      appointmentDate,
      appointmentTime,
    };

    console.log("Sending appointment data:", appointment);

    setLoading(true);

    try {
      const response = await fetch("http://localhost:7010/appointments/book", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(appointment),
      });

      const responseData = await response.text();
      console.log("Response data:", responseData);

      try {
        const jsonResponse = JSON.parse(responseData);
        if (response.ok) {
          toast.success(jsonResponse.message || "Booking confirmed, check your email for booking confirmation.");
          onSubmit(jsonResponse);
          setTimeout(() => {
            onClose(); // Close the modal after 5 seconds
          }, 5000);
        } else {
          toast.error(`Booking failed: ${jsonResponse.message}`);
        }
      } catch (e) {
        if (response.ok) {
          toast.success(
            "Booking confirmed, check your email for booking confirmation."
          );
          setTimeout(() => {
            onClose(); // Close the modal after 5 seconds
          }, 5000);
        } else {
          toast.error(responseData);
        }
      }
    } catch (error) {
      console.error("Error:", error);
      if (error.message.includes("503")) {
        toast.error("Service is currently unavailable. Please try again later.");
      } else {
        toast.error(`An error occurred: ${error.message}`);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <h2 className="modal__header">
        Book Your Appointment <br />
        in just <span className="highlight">5 minutes</span>
      </h2>
      {loading ? (
        <div className="spinner">
          <ClipLoader size={50} color={"#123abc"} loading={loading} />
        </div>
      ) : (
        <form className="modal__form" onSubmit={handleSubmit}>
          <label>Name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <label>Appointment Date</label>
          <input
            type="date"
            value={appointmentDate}
            onChange={(e) => setAppointmentDate(e.target.value)}
            required
          />
          <label>Appointment Time</label>
          <select
            value={appointmentTime}
            onChange={(e) => setAppointmentTime(e.target.value)}
            required
          >
            <option value="06:00">06:00 AM</option>
            <option value="07:00">07:00 AM</option>
            <option value="08:00">08:00 AM</option>
            <option value="09:00">09:00 AM</option>
            <option value="10:00">10:00 AM</option>
            <option value="11:00">11:00 AM</option>
            <option value="12:00">12:00 PM</option>
            <option value="13:00">01:00 PM</option>
            <option value="14:00">02:00 PM</option>
            <option value="15:00">03:00 PM</option>
            <option value="16:00">04:00 PM</option>
            <option value="17:00">05:00 PM</option>
            <option value="18:00">06:00 PM</option>
            <option value="19:00">07:00 PM</option>
            <option value="20:00">08:00 PM</option>
            <option value="21:00">09:00 PM</option>
            <option value="22:00">10:00 PM</option>
          </select>
          <button className="btn" type="submit">
            Book Appointment
          </button>
        </form>
      )}
    </>
  );
};

export default BookingForm;
