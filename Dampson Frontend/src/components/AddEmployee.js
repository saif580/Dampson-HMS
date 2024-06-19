import React, { useEffect, useState } from "react";
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./AddEmployee.css";
import Modal from "./Modal";

const AddEmployee = () => {
  const [employees, setEmployees] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newEmployee, setNewEmployee] = useState({
    username: "",
    password: "",
    role: "RECEPTIONIST", // Set the default value to "RECEPTIONIST"
  });
  const [loading, setLoading] = useState(false);
  const [isInitialFetch, setIsInitialFetch] = useState(true); // To check if it's the initial data fetch

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await fetch("http://localhost:7010/users", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) {
          throw new Error(`Error fetching employees: ${response.statusText}`);
        }

        const data = await response.json();
        setEmployees(data);
        if (isInitialFetch) {
          toast.success("Employees data fetched successfully!");
          setIsInitialFetch(false); // Ensure the toast message is only shown once
        }
      } catch (error) {
        console.error("Error fetching employees:", error);
        toast.error(`Error fetching employees: ${error.message}`);
      }
    };

    fetchEmployees();
  }, [isInitialFetch]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewEmployee((prevEmployee) => ({
      ...prevEmployee,
      [name]: value,
    }));
  };

  const handleAddEmployee = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch("http://localhost:7010/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify(newEmployee),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error adding employee: ${errorText}`);
      }

      const data = await response.json();
      setEmployees((prevEmployees) => [...prevEmployees, data]);
      setIsModalOpen(false);
      setNewEmployee({ username: "", password: "", role: "RECEPTIONIST" });
      toast.success("Employee added successfully!");
    } catch (error) {
      console.error("Error adding employee:", error);
      toast.error(`${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteEmployee = async (userId) => {
    try {
      const response = await fetch(`http://localhost:7010/users/${userId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Error deleting employee: ${response.statusText}`);
      }

      setEmployees((prevEmployees) =>
        prevEmployees.filter((employee) => employee.userId !== userId)
      );
      toast.success("Employee deleted successfully!");
    } catch (error) {
      console.error("Error deleting employee:", error);
      toast.error(`${error.message}`);
    }
  };

  return (
    <div className="employee-list">
      <h2>Employee Management</h2>
      <button className="btn" onClick={() => setIsModalOpen(true)}>
        Add Employee
      </button>
      
      {loading ? (
        <div className="spinner">
          <ClipLoader size={50} color={"#123abc"} loading={loading} />
        </div>
      ) : (
        <>
          <table className="employee-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Role</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {employees.map((employee) => (
                <tr key={employee.userId}>
                  <td>{employee.userId}</td>
                  <td>{employee.username}</td>
                  <td>{employee.role}</td>
                  <td>
                    {employee.userId !== 1 && (
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteEmployee(employee.userId)}
                      >
                        Delete
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <Modal
            isOpen={isModalOpen}
            onClose={() => setIsModalOpen(false)}
            className="employee-modal"
          >
            <h2 className="modal__header">Add New Employee</h2>
            <form className="modal__form" onSubmit={handleAddEmployee}>
              <label>Username</label>
              <input
                type="text"
                name="username"
                value={newEmployee.username}
                onChange={handleInputChange}
                required
              />
              <label>Password</label>
              <input
                type="password"
                name="password"
                value={newEmployee.password}
                onChange={handleInputChange}
                required
              />
              <label>Role</label>
              <input
                type="text"
                name="role"
                value={newEmployee.role}
                onChange={handleInputChange}
                disabled
                required
              />
              <button className="btn" type="submit" disabled={loading}>
                {loading ? <ClipLoader size={20} color={"#fff"} /> : "Add Employee"}
              </button>
            </form>
          </Modal>
        </>
      )}

      <ToastContainer />
    </div>
  );
};

export default AddEmployee;
