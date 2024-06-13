// import React, { useEffect, useState } from 'react';
// import './EmployeeList.css';
// import Modal from './Modal';

// const EmployeeList = () => {
//   const [employees, setEmployees] = useState([]);
//   const [isModalOpen, setIsModalOpen] = useState(false);
//   const [newEmployee, setNewEmployee] = useState({ username: '', password: '', role: '' });

//   useEffect(() => {
//     const fetchEmployees = async () => {
//       try {
//         const response = await fetch('http://localhost:7010/users', {
//           headers: {
//             'Authorization': `Bearer ${localStorage.getItem('token')}`
//           }
//         });

//         if (!response.ok) {
//           throw new Error(`Error fetching employees: ${response.statusText}`);
//         }

//         const data = await response.json();
//         setEmployees(data);
//       } catch (error) {
//         console.error('Error fetching employees:', error);
//       }
//     };

//     fetchEmployees();
//   }, []);

//   const handleInputChange = (e) => {
//     const { name, value } = e.target;
//     setNewEmployee((prevEmployee) => ({
//       ...prevEmployee,
//       [name]: value,
//     }));
//   };

//   const handleAddEmployee = async (e) => {
//     e.preventDefault();
//     try {
//       const response = await fetch('http://localhost:7010/users', {
//         method: 'POST',
//         headers: {
//           'Content-Type': 'application/json',
//           'Authorization': `Bearer ${localStorage.getItem('token')}`
//         },
//         body: JSON.stringify(newEmployee),
//       });

//       if (!response.ok) {
//         throw new Error(`Error adding employee: ${response.statusText}`);
//       }

//       const data = await response.json();
//       setEmployees((prevEmployees) => [...prevEmployees, data]);
//       setIsModalOpen(false);
//       setNewEmployee({ username: '', password: '', role: '' });
//     } catch (error) {
//       console.error('Error adding employee:', error);
//     }
//   };

//   return (
//     <div className="employee-list">
//       <h2>All Employees</h2>
//       <button className="btn" onClick={() => setIsModalOpen(true)}>Add Employee</button>
//       <table>
//         <thead>
//           <tr>
//             <th>ID</th>
//             <th>Username</th>
//             <th>Role</th>
//           </tr>
//         </thead>
//         <tbody>
//                     {employees.map((employee) => (
//             <tr key={employee.id}>
//               <td>{employee.id}</td>
//               <td>{employee.username}</td>
//               <td>{employee.role}</td>
//             </tr>
//           ))}
//         </tbody>
//       </table>

//       <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
//         <h2 className="modal__header">Add New Employee</h2>
//         <form className="modal__form" onSubmit={handleAddEmployee}>
//           <label>Username</label>
//           <input
//             type="text"
//             name="username"
//             value={newEmployee.username}
//             onChange={handleInputChange}
//             required
//           />
//           <label>Password</label>
//           <input
//             type="password"
//             name="password"
//             value={newEmployee.password}
//             onChange={handleInputChange}
//             required
//           />
//           <label>Role</label>
//           <input
//             type="text"
//             name="role"
//             value={newEmployee.role}
//             onChange={handleInputChange}
//             required
//           />
//           <button className="btn" type="submit">Add Employee</button>
//         </form>
//       </Modal>
//     </div>
//   );
// };

// export default EmployeeList;
