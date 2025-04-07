import React from 'react';

function LoginPage() {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">Benvenuto! Effettua il login</h1>
      <a href="/oauth2/authorization/google" className="bg-blue-500 text-white py-2 px-4 rounded-lg">
        Login con Google
      </a>
    </div>
  );
}

export default LoginPage;