import React from "react";

function Profile() {
  return (
    <div className="p-6 max-w-md mx-auto mt-10 bg-white rounded-lg shadow-md text-gray-800">
      {/* Heading */}
      <h1 className="text-2xl font-bold mb-4 text-center">Profile</h1>

      {/* Email info */}
      <p className="text-lg mb-6 text-center">
        Email: <span className="font-medium">user@example.com</span>
      </p>

      {/* Enable Two-Factor Auth button */}
      <div className="flex justify-center">
        <button className="bg-teal-500 hover:bg-teal-600 text-white font-medium px-5 py-2 rounded-md transition duration-200">
          Enable Two-Factor Authentication
        </button>
      </div>
    </div>
  );
}

export default Profile;
