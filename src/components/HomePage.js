import React from 'react';

function HomePage() {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">Le tue attività</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <div className="bg-white p-4 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold">Picnic al Parco</h2>
          <p className="text-gray-600">Un evento per rilassarsi all'aria aperta.</p>
          <button className="mt-4 bg-blue-500 text-white py-2 px-4 rounded-lg">Partecipa</button>
        </div>
        {/* Altri eventi */}
      </div>
    </div>
  );
}

export default HomePage;