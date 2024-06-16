import React, { useEffect, useState } from 'react';
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import './Appointment.css';

const Appointment = () => {
  const [appointments, setAppointments] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  const [lazyLoadCount, setLazyLoadCount] = useState(9);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchAppointments = async () => {
      setLoading(true);
      try {
        const response = await fetch('http://localhost:7010/api/user-appointments', {
          method: "GET",
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`Error fetching appointments: ${response.statusText}`);
        }

        const data = await response.json();
        setAppointments(sortAppointments(data));
        toast.success("Appointments data fetched successfully!");
      } catch (error) {
        console.error('Error fetching appointments:', error);
        toast.error('Error fetching appointments.');
      } finally {
        setLoading(false);
      }
    };

    fetchAppointments();
  }, []);

  const sortAppointments = (appointments) => {
    const today = new Date().toISOString().split('T')[0];

    const sortedAppointments = appointments.sort((a, b) => {
      if (a.appointmentDate === b.appointmentDate) {
        return new Date(`1970-01-01T${a.appointmentTime}`) - new Date(`1970-01-01T${b.appointmentTime}`);
      }
      return new Date(a.appointmentDate) - new Date(b.appointmentDate);
    });

    const todayAppointments = sortedAppointments.filter(appointment => appointment.appointmentDate >= today);

    return todayAppointments;
  };

  const loadMoreAppointments = () => {
    setLazyLoadCount(prevCount => prevCount + 9);
  };

  const filteredAppointments = appointments
    .filter(appointment => {
      const matchesDate = selectedDate ? appointment.appointmentDate === selectedDate : true;
      const matchesName = appointment.name.toLowerCase().includes(searchTerm.toLowerCase());
      return matchesDate && matchesName;
    })
    .slice(0, lazyLoadCount);

  return (
    <div className="appointment">
      <h2>All Appointments</h2>
      <div className="filters">
        <input
          type="text"
          placeholder="Search by name"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <input
          type="date"
          value={selectedDate}
          onChange={(e) => setSelectedDate(e.target.value)}
        />
      </div>
      {loading ? (
        <div className="spinner">
          <ClipLoader size={50} color={"#123abc"} loading={loading} />
        </div>
      ) : (
        <>
          <table className="appointment-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Appointment Date</th>
                <th>Appointment Time</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredAppointments.map((appointment) => (
                <tr key={appointment.id}>
                  <td>{appointment.id}</td>
                  <td>{appointment.name}</td>
                  <td>{appointment.email}</td>
                  <td>{appointment.appointmentDate}</td>
                  <td>{appointment.appointmentTime}</td>
                  <td>{appointment.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {filteredAppointments.length < appointments.length && (
            <button className='load-more-button' onClick={loadMoreAppointments}>Load More</button>
          )}
        </>
      )}
      <ToastContainer />
    </div>
  );
};

export default Appointment;
