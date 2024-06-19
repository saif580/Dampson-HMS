import React, { useContext, useEffect, useState } from "react";
import ClipLoader from "react-spinners/ClipLoader";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AuthContext } from "../context/AuthContext";
import "./Settings.css";

const Settings = () => {
  const { username } = useContext(AuthContext); // Get username from AuthContext
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [loading, setLoading] = useState(false);

  console.log("Username:", username); // Debugging line

  useEffect(() => {
    toast.success("Settings fetched successfully!");
  }, []);

  const handleUpdatePassword = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:7010/users/${username}/update-password`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
          body: JSON.stringify({ currentPassword, newPassword }),
        }
      );

      if (response.ok) {
        toast.success("Password updated successfully!");
      } else {
        const errorData = await response.json();
        throw new Error(`Error updating password: ${errorData.message}`);
      }
    } catch (error) {
      console.error("Error updating password:", error);
      toast.error(`Error updating password: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="settings">
      <h2 className="settings__header">Update Password</h2>
      <form className="settings__form" onSubmit={handleUpdatePassword}>
        <label className="settings__label">Current Password</label>
        <input
          type="password"
          className="settings__input"
          value={currentPassword}
          onChange={(e) => setCurrentPassword(e.target.value)}
          required
          autoComplete="current-password"
        />
        <label className="settings__label">New Password</label>
        <input
          type="password"
          className="settings__input"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          required
          autoComplete="new-password"
        />
        <button className="settings__btn" type="submit" disabled={loading}>
          {loading ? (
            <ClipLoader size={20} color={"#fff"} />
          ) : (
            "Update Password"
          )}
        </button>
      </form>
      <ToastContainer />
    </div>
  );
};

export default Settings;
