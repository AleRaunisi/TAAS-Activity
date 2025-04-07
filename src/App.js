import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import CreateActivityPage from './pages/CreateActivityPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/create-activity" element={<CreateActivityPage />} />
      </Routes>
    </Router>
  );
}

export default App;