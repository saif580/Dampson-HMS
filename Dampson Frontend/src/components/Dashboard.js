import {
  ArcElement,
  BarElement,
  CategoryScale,
  Chart,
  DoughnutController,
  LinearScale,
} from "chart.js";
import React, { useContext, useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import {
  FaBars,
  FaBell,
  FaClipboardList,
  FaCog,
  FaMoneyBillWave,
  FaSignOutAlt,
  FaUserMd,
} from "react-icons/fa";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import Appointment from "./Appointment";
import BillingRecords from "./BillingRecords";
import "./Dashboard.css";
import PatientRecords from "./PatientRecords";
import Prescription from "./Prescription";
import Settings from "./Settings";

// Register the necessary Chart.js components
Chart.register(
  BarElement,
  CategoryScale,
  LinearScale,
  DoughnutController,
  ArcElement
);

const DoctorDashboard = () => {
  const { username } = useContext(AuthContext);
  const [activeMenu, setActiveMenu] = useState("Dashboard");
  const [visitors, setVisitors] = useState(0);
  const [appointments, setAppointments] = useState(0);
  const [newAppointments, setNewAppointments] = useState(0);
  const [numberOfBills, setNumberOfBills] = useState(0);
  const [barData, setBarData] = useState({
    labels: [],
    datasets: [
      {
        label: "Appointments",
        data: [],
        backgroundColor: [
          "#666666",
          "#666666",
          "#666666",
          "#666666",
          "#666666",
          "#666666",
          "#666666",
          "#666666",
          "#666666",
        ],
      },
    ],
  });
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  useEffect(() => {
    const fetchVisitors = async () => {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/patients`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (!response.ok) {
          throw new Error("Failed to fetch visitors");
        }
        const data = await response.json();
        setVisitors(data.length);
      } catch (error) {
        console.error("Error fetching visitors:", error);
      }
    };

    const fetchAppointments = async () => {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/api/user-appointments`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (!response.ok) {
          throw new Error("Failed to fetch appointments");
        }
        const data = await response.json();

        setAppointments(data.length); // Set total appointments

        // Process the data for the last 4 days and next 5 days
        const today = new Date();
        const lastFourAndNextFiveDays = [];
        for (let i = -4; i <= 5; i++) {
          const date = new Date();
          date.setDate(today.getDate() + i);
          const dateString = date.toISOString().split("T")[0];
          lastFourAndNextFiveDays.push(dateString);
        }

        const appointmentCounts = lastFourAndNextFiveDays.map((day) => {
          const count = data.filter((appointment) => {
            const appointmentDate = appointment.appointmentDate
              ? appointment.appointmentDate.split("T")[0]
              : null;
            return appointmentDate === day;
          }).length;
          return count;
        });

        setBarData({
          labels: lastFourAndNextFiveDays,
          datasets: [
            {
              label: "Appointments",
              data: appointmentCounts,
              backgroundColor: [
                "#a3d5e6",
                "#4682b4",
                "#3c99dc",
                "#2565ae",
                "#a3d5e6",
                "#4682b4",
                "#3c99dc",
                "#2565ae",
                "#4682b4",
              ],
            },
          ],
        });

        const storedAppointments =
          JSON.parse(localStorage.getItem("appointments")) || [];
        const newAppointmentsCount = data.filter(
          (appointment) =>
            !storedAppointments.some((stored) => stored.id === appointment.id)
        ).length;

        setNewAppointments(newAppointmentsCount);
        localStorage.setItem("appointments", JSON.stringify(data));

        // Store the timestamp for new appointments
        const newAppointmentTimestamp = new Date().getTime();
        localStorage.setItem(
          "newAppointmentTimestamp",
          newAppointmentTimestamp
        );

        // Clear the badge after 5 minutes
        setTimeout(() => {
          setNewAppointments(0);
        }, 5 * 60 * 1000); // 5 minutes in milliseconds
      } catch (error) {
        console.error("Error fetching appointments:", error);
      }
    };

    const fetchNumberOfBills = async () => {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/billings`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (!response.ok) {
          throw new Error("Failed to fetch number of bills");
        }
        const numberOfBills = await response.json();
        setNumberOfBills(numberOfBills.length);
      } catch (error) {
        console.error("Error fetching number of bills:", error);
      }
    };

    fetchVisitors();
    fetchAppointments();
    fetchNumberOfBills();

    const interval = setInterval(() => {
      fetchAppointments();
    }, 60000); // fetch appointments every minute

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    const newAppointmentTimestamp = localStorage.getItem(
      "newAppointmentTimestamp"
    );
    if (newAppointmentTimestamp) {
      const timeElapsed = new Date().getTime() - newAppointmentTimestamp;

      if (timeElapsed < 5 * 60 * 1000) {
        // Less than 5 minutes have passed
        const storedAppointments =
          JSON.parse(localStorage.getItem("appointments")) || [];
        const newAppointmentsCount = storedAppointments.length;
        setNewAppointments(newAppointmentsCount);
        setTimeout(() => {
          setNewAppointments(0);
        }, 5 * 60 * 1000 - timeElapsed); // Time left to complete 5 minutes
      }
    }
  }, []);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const handleMenuClick = (menu) => {
    setActiveMenu(menu);
    setIsMobileMenuOpen(false);
  };

  const renderContent = () => {
    switch (activeMenu) {
      case "Appointments":
        return <Appointment />;
      case "Records":
        return <PatientRecords />;
      case "Prescription":
        return <Prescription />;
      case "Settings":
        return <Settings />;
      case "Bill":
        return <BillingRecords />;
      case "Dashboard":
      default:
        return (
          <>
            <div className="cards">
              <div className="card">
                <h3>Total Appointments</h3>
                <p>{appointments}</p>
              </div>
              <div className="card">
                <h3>Visitors</h3>
                <p>{visitors}</p>
              </div>
              <div className="card">
                <h3>Number Of Bills Generated</h3>
                <p>{numberOfBills}</p>
              </div>
            </div>
            <div
              className="chart"
              style={{ width: "100%", padding: "0", margin: "0" }}
            >
              <h3>Appointments in Last 4 and Next 5 Days</h3>
              <div
                className="chart-container"
                style={{ width: "100%", padding: "0", margin: "0" }}
              >
                <Bar data={barData} options={{ responsive: true }} />
              </div>
            </div>
          </>
        );
    }
  };

  return (
    <div className="dashboard">
      <div className="sidebar">
        <div className="logo-menu-container">
          <div className="logo">
            <img src="/img/Dampson-dashboard1.png" alt="Dampson HMS" />
          </div>
          <div className="menu-toggle" onClick={toggleMobileMenu}>
            <FaBars />
          </div>
        </div>
        <ul className={isMobileMenuOpen ? "show" : ""}>
          <li
            className={activeMenu === "Dashboard" ? "active" : ""}
            onClick={() => handleMenuClick("Dashboard")}
          >
            <FaClipboardList /> Dashboard
          </li>
          <li
            className={activeMenu === "Appointments" ? "active" : ""}
            onClick={() => handleMenuClick("Appointments")}
          >
            <FaUserMd /> Appointments
          </li>
          <li
            className={activeMenu === "Records" ? "active" : ""}
            onClick={() => handleMenuClick("Records")}
          >
            <FaClipboardList /> Records
          </li>
          <li
            className={activeMenu === "Bill" ? "active" : ""}
            onClick={() => handleMenuClick("Bill")}
          >
            <FaMoneyBillWave /> Bill
          </li>
          <li
            className={activeMenu === "Settings" ? "active" : ""}
            onClick={() => handleMenuClick("Settings")}
          >
            <FaCog /> Settings
          </li>
          <li>
            <Link to="/logout" className="logout-btn">
              <FaSignOutAlt /> Logout
            </Link>
          </li>
        </ul>
      </div>
      <div className="main-content">
        <header>
          <h2>Welcome, {username}</h2>
          <div className="user-info">
            <FaBell />
            {newAppointments > 0 && (
              <span className="notification-badge">{newAppointments}</span>
            )}
          </div>
        </header>
        {renderContent()}
      </div>
    </div>
  );
};

export default DoctorDashboard;
