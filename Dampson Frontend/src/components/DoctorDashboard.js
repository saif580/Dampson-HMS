import { ArcElement, BarElement, CategoryScale, Chart, DoughnutController, LinearScale } from 'chart.js';
import React, { useContext, useState } from 'react';
import { Bar, Doughnut } from 'react-chartjs-2';
import { FaBell, FaClipboardList, FaCog, FaMoneyBillWave, FaSignOutAlt, FaUserMd, FaUserPlus, FaUserTie } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import AddEmployee from './AddEmployee';
import Appointment from './Appointment';
import Clinic from './Clinic';
import './Dashboard.css';
import PatientRecords from './PatientRecords';
import Prescription from './Prescription';

// Register the necessary Chart.js components
Chart.register(BarElement, CategoryScale, LinearScale, DoughnutController, ArcElement);

const DoctorDashboard = () => {
  const { username } = useContext(AuthContext);
  const [activeMenu, setActiveMenu] = useState('Dashboard');

  const barData = {
    labels: ['06/04/2024', '06/05/2024', '06/06/2024', '06/07/2024'],
    datasets: [
      {
        label: 'Appointments',
        data: [50, 70, 100, 80],
        backgroundColor: ['#666666', '#666666', '#666666', '#666666']
      }
    ]
  };

  const doughnutData = {
    labels: ['0-20', '20-50', '50>'],
    datasets: [
      {
        data: [3000, 2000, 1200],
        backgroundColor: ['#666666', '#999999', '#cccccc']
      }
    ]
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
      case 'Dashboard':
      default:
        return (
          <>
            <div className="cards">
              <div className="card">
                <h3>Appointments</h3>
                <p>70</p>
              </div>
              <div className="card">
                <h3>Visitors</h3>
                <p>12,302</p>
              </div>
              <div className="card">
                <h3>Refunds</h3>
                <p>963</p>
              </div>
            </div>
            <div className="charts">
              <div className="chart">
                <h3>All Appointments</h3>
                <div className="chart-container">
                  <Bar data={barData} />
                </div>
              </div>
              <div className="chart">
                <h3>Top Categories</h3>
                <div className="chart-container">
                  <Doughnut data={doughnutData} />
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
          <img src="/img/Dampson (1).gif" alt="Dampson HMS" />
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
