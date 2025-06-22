
import React from 'react';
import { useTheme } from '../contexts/ThemeContext';

interface NetworkFilterProps {
  currentFilter: 'all' | 'http' | 'websocket';
  onFilterChange: (filter: 'all' | 'http' | 'websocket') => void;
}

export const NetworkFilter: React.FC<NetworkFilterProps> = ({ currentFilter, onFilterChange }) => {
  const { theme } = useTheme();
  
  const filters = [
    { key: 'all' as const, label: 'All', icon: '🌐' },
    { key: 'http' as const, label: 'HTTP', icon: '📡' },
    { key: 'websocket' as const, label: 'WebSocket', icon: '🔌' }
  ];

  return (
    <div className={`flex space-x-1 rounded-lg p-1 transition-colors duration-300 ${
      theme === 'dark' ? 'bg-gray-800' : 'bg-white border border-gray-200'
    }`}>
      {filters.map((filter) => (
        <button
          key={filter.key}
          onClick={() => onFilterChange(filter.key)}
          className={`flex-1 px-3 py-2 rounded-md text-sm font-medium transition-all duration-200 ${
            currentFilter === filter.key
              ? theme === 'dark'
                ? 'bg-blue-600 text-white shadow-lg'
                : 'bg-blue-500 text-white shadow-lg'
              : theme === 'dark'
                ? 'text-gray-400 hover:text-white hover:bg-gray-700'
                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
          }`}
        >
          <span className="mr-2">{filter.icon}</span>
          {filter.label}
        </button>
      ))}
    </div>
  );
};
