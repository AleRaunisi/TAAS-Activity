import React from 'react';

function CreateActivityPage() {
  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Crea una nuova attività</h1>
      <form action="/api/attivita" method="POST" className="space-y-4">
        <div>
          <label htmlFor="titolo" className="block text-sm font-medium text-gray-700">Titolo</label>
          <input type="text" id="titolo" name="titolo" className="w-full px-4 py-2 border rounded-lg" required />
        </div>
        <div>
          <label htmlFor="descrizione" className="block text-sm font-medium text-gray-700">Descrizione</label>
          <textarea id="descrizione" name="descrizione" className="w-full px-4 py-2 border rounded-lg" required></textarea>
        </div>
        <div className="flex justify-between">
          <button type="submit" className="bg-blue-500 text-white py-2 px-6 rounded-lg">Crea</button>
          <button type="button" className="bg-gray-200 text-gray-700 py-2 px-6 rounded-lg">Annulla</button>
        </div>
      </form>
    </div>
  );
}

export default CreateActivityPage;