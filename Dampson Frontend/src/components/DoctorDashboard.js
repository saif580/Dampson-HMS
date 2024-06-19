import { ArcElement, BarElement, CategoryScale, Chart, DoughnutController, LinearScale, Tooltip } from 'chart.js';
import React, { useContext, useEffect, useState } from 'react';
import { Bar, Doughnut } from 'react-chartjs-2';
import { FaBell, FaClipboardList, FaCog, FaMoneyBillWave, FaSignOutAlt, FaUserMd, FaUserPlus, FaUserTie } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import AddEmployee from './AddEmployee';
import Appointment from './Appointment';
import BillingRecords from './BillingRecords';
import Clinic from './Clinic';
import './Dashboard.css';
import PatientRecords from './PatientRecords';
import Prescription from './Prescription';
import Settings from './Settings';

// Register the necessary Chart.js components
Chart.register(BarElement, CategoryScale, LinearScale, DoughnutController, ArcElement, Tooltip);

const DoctorDashboard = () => {
  const { username } = useContext(AuthContext);
  const [activeMenu, setActiveMenu] = useState('Dashboard');
  const [visitors, setVisitors] = useState(0);
  const [appointments, setAppointments] = useState(0);
  const [totalEarnings, setTotalEarnings] = useState(0);
  const [paymentMethodsData, setPaymentMethodsData] = useState({
    labels: ['Cash', 'Online', 'Prepaid'],
    datasets: [
      {
        data: [0, 0, 0],
        backgroundColor: ['#666666', '#999999', '#cccccc'],
      },
    ],
  });
  const [barData, setBarData] = useState({
    labels: [],
    datasets: [
      {
        label: 'Appointments',
        data: [],
        backgroundColor: ['#666666', '#666666', '#666666', '#666666'],
      },
    ],
  });

  useEffect(() => {
    const fetchVisitors = async () => {
      try {
        const response = await fetch('http://localhost:7010/patients', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch visitors');
        }
        const data = await response.json();
        setVisitors(data.length);
      } catch (error) {
        console.error('Error fetching visitors:', error);
      }
    };

    const fetchAppointments = async () => {
      try {
        const response = await fetch('http://localhost:7010/api/user-appointments', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch appointments');
        }
        const data = await response.json();
        
        console.log("Fetched Appointments Data: ", data);

        // Process the data for the last 4 days
        const today = new Date();
        const lastFourDays = [];
        for (let i = 3; i >= 0; i--) {
          const date = new Date();
          date.setDate(today.getDate() - i);
          const dateString = date.toISOString().split('T')[0];
          lastFourDays.push(dateString);
        }

        const appointmentCounts = lastFourDays.map((day) => {
          const count = data.filter(
            (appointment) => {
              const appointmentDate = appointment.appointmentDate ? appointment.appointmentDate.split('T')[0] : null;
              console.log("Processing Date: ", appointmentDate, " Expected Date: ", day);
              return appointmentDate === day;
            }
          ).length;
          return count;
        });

        console.log("Appointment Counts: ", appointmentCounts);

        setBarData({
          labels: lastFourDays,
          datasets: [
            {
              label: 'Appointments',
              data: appointmentCounts,
              backgroundColor: ['#666666', '#666666', '#666666', '#666666'],
            },
          ],
        });

        setAppointments(data.length);
      } catch (error) {
        console.error('Error fetching appointments:', error);
      }
    };

    const fetchTotalEarnings = async () => {
      try {
        const response = await fetch('http://localhost:7010/billings/total-earnings', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch total earnings');
        }
        const totalEarnings = await response.json();
        setTotalEarnings(totalEarnings);
      } catch (error) {
        console.error('Error fetching total earnings:', error);
      }
    };

    const fetchPaymentMethodsSummary = async () => {
      try {
        const response = await fetch('http://localhost:7010/billings/payment-methods-summary', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch payment methods summary');
        }
        const data = await response.json();
        setPaymentMethodsData({
          labels: ['Cash', 'Online', 'Prepaid'],
          datasets: [
            {
              data: [data.Cash || 0, data.Online || 0, data.Prepaid || 0],
              backgroundColor: ['#666666', '#999999', '#cccccc'],
            },
          ],
        });
      } catch (error) {
        console.error('Error fetching payment methods summary:', error);
      }
    };

    fetchVisitors();
    fetchAppointments();
    fetchTotalEarnings();
    fetchPaymentMethodsSummary();
  }, []);

  const doughnutOptions = {
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context) {
            const label = context.label || '';
            const value = context.raw || 0;
            return `${label}`;
          },
        },
      },
    },
  };

  const renderContent = () => {
    switch (activeMenu) {
      case 'Appointments':
        return <Appointment />;
      case 'AddEmployee':
        return <AddEmployee />;
      case 'Clinic':
        return <Clinic />;
      case 'Records':
        return <PatientRecords />;
      case 'Prescription':
        return <Prescription />;
      case 'Settings':
        return <Settings />;
      case 'Bill': 
        return <BillingRecords />;
      case 'Dashboard':
      default:
        return (
          <>
            <div className="cards">
              <div className="card" title="Total number of appointments">
                <h3>Appointments</h3>
                <p>{appointments}</p>
              </div>
              <div className="card" title="Total number of visitors">
                <h3>Visitors</h3>
                <p>{visitors}</p>
              </div>
              <div className="card" title="Total earnings">
                <h3>Total Earnings</h3>
                <p>Rs. {totalEarnings}</p>
              </div>
            </div>
            <div className="charts">
              <div className="chart">
                <h3>All Appointments</h3>
                <div className="chart-container">
                  <Bar 
                    data={barData}
                    options={{
                      scales: {
                        y: {
                          beginAtZero: true,
                          ticks: {
                            stepSize: 5, // Set the step size to 5
                          },
                        },
                      },
                    }}
                  />
                </div>
              </div>
              <div className="chart">
                <h3>Payment Methods</h3>
                <div className="chart-container">
                  <Doughnut data={paymentMethodsData} options={doughnutOptions} />
                </div>
              </div>
            </div>
          </>
        );
    }
  };

  return (
    <div className="dashboard">
      <div className="sidebar">
        <div className="logo">
          <img src="/img/Dampson-dashboard.png" alt="Dampson HMS" />
        </div>
        <ul>
          <li className={activeMenu === 'Dashboard' ? 'active' : ''} onClick={() => setActiveMenu('Dashboard')}><FaClipboardList /> Dashboard</li>
          <li className={activeMenu === 'Appointments' ? 'active' : ''} onClick={() => setActiveMenu('Appointments')}><FaUserMd /> Appointments</li>
          <li className={activeMenu === 'Prescription' ? 'active' : ''} onClick={() => setActiveMenu('Prescription')}><FaClipboardList /> Prescription</li>
          <li className={activeMenu === 'Records' ? 'active' : ''} onClick={() => setActiveMenu('Records')}><FaClipboardList /> Records</li>
          <li className={activeMenu === 'Bill' ? 'active' : ''} onClick={() => setActiveMenu('Bill')}><FaMoneyBillWave /> Bill</li>
          <li className={activeMenu === 'Clinic' ? 'active' : ''} onClick={() => setActiveMenu('Clinic')}><FaUserPlus /> Clinic</li>
          <li className={activeMenu === 'AddEmployee' ? 'active' : ''} onClick={() => setActiveMenu('AddEmployee')}><FaUserTie /> Add Employee</li>
          <li className={activeMenu === 'Settings' ? 'active' : ''} onClick={() => setActiveMenu('Settings')}><FaCog /> Settings</li>
        </ul>
        <Link to="/logout" className="logout-btn"><FaSignOutAlt /> Logout</Link>
      </div>
      <div className="main-content">
        <header>
          <h2>Welcome, Dr. {username}</h2>
          <div className="user-info">
            <FaBell />
          </div>
        </header>
        {renderContent()}
      </div>
    </div>
  );
};

export default DoctorDashboard;
