import _, { useState, useEffect } from 'react';
import { NetworkLogItem } from '../components/NetworkLogItem';
import { NetworkFilter } from '../components/NetworkFilter';
import type { NetworkLog } from '../types/NetworkLog';
import { useTheme } from '../contexts/ThemeContext';
import { Moon, Sun } from 'lucide-react';

// Mock data showcasing different request types and their color schemes
const generateMockLogs = (): NetworkLog[] => [
  {
    id: '1',
    type: 'http',
    method: 'GET',
    url: 'https://api.example.com/users',
    status: 200,
    timestamp: new Date(Date.now() - 5000),
    duration: 245,
    size: 1024
  },
  {
    id: '2',
    type: 'http',
    method: 'DELETE',
    url: 'https://api.example.com/users/123',
    status: 204,
    timestamp: new Date(Date.now() - 10000),
    duration: 156,
    size: 0
  },
  {
    id: '3',
    type: 'http',
    method: 'POST',
    url: 'https://api.example.com/login',
    status: 401,
    timestamp: new Date(Date.now() - 15000),
    duration: 156,
    size: 512
  },
  {
    id: '4',
    type: 'http',
    method: 'GET',
    url: 'https://api.example.com/data',
    status: 500,
    timestamp: new Date(Date.now() - 20000),
    duration: 2340,
    size: 256
  },
  {
    id: '5',
    type: 'websocket',
    event: 'connect',
    url: 'wss://socket.example.com',
    timestamp: new Date(Date.now() - 25000),
    direction: 'outgoing',
    wsStatus: 'opened',
    data: 'Connection established'
  },
  {
    id: '6',
    type: 'http',
    method: 'PUT',
    url: 'https://api.example.com/users/456',
    status: 200,
    timestamp: new Date(Date.now() - 30000),
    duration: 189,
    size: 768
  },
  {
    id: '7',
    type: 'websocket',
    event: 'message',
    url: 'wss://socket.example.com',
    timestamp: new Date(Date.now() - 35000),
    direction: 'incoming',
    wsStatus: 'opened',
    data: '{"type":"notification","message":"New update available"}'
  },
  {
    id: '8',
    type: 'http',
    method: 'DELETE',
    url: 'https://api.example.com/posts/789',
    status: 404,
    timestamp: new Date(Date.now() - 40000),
    duration: 98,
    size: 128
  },
  {
    id: '9',
    type: 'websocket',
    event: 'disconnect',
    url: 'wss://socket.example.com',
    timestamp: new Date(Date.now() - 50000),
    direction: 'outgoing',
    wsStatus: 'closed',
    data: 'Connection closed'
  }
];

const NetworkLogs = () => {
  const [logs, setLogs] = useState<NetworkLog[]>([]);
  const [filteredLogs, setFilteredLogs] = useState<NetworkLog[]>([]);
  const [filter, setFilter] = useState<'all' | 'http' | 'websocket'>('all');
  const { theme, toggleTheme } = useTheme();

  useEffect(() => {
    const mockLogs = generateMockLogs();
    setLogs(mockLogs);
    setFilteredLogs(mockLogs);
  }, []);

  useEffect(() => {
    if (filter === 'all') {
      setFilteredLogs(logs);
    } else {
      setFilteredLogs(logs.filter(log => log.type === filter));
    }
  }, [logs, filter]);

  const clearLogs = () => {
    setLogs([]);
    setFilteredLogs([]);
  };

  return (
    <div className={`min-h-screen transition-colors duration-300 ${
      theme === 'dark' 
        ? 'bg-gray-900 text-white' 
        : 'bg-gray-50 text-gray-900'
    }`}>
      {/* Header */}
      <div className={`sticky top-0 z-10 backdrop-blur-sm border-b transition-colors duration-300 ${
        theme === 'dark' 
          ? 'bg-gray-900/95 border-gray-800' 
          : 'bg-white/95 border-gray-200'
      }`}>
        <div className="px-4 py-4">
          <div className="flex items-center justify-between mb-4">
            <h1 className="text-xl font-semibold">Network Logs</h1>
            <div className="flex items-center space-x-3">
              <button
                onClick={toggleTheme}
                className={`p-2 rounded-lg transition-colors duration-200 ${
                  theme === 'dark'
                    ? 'text-gray-400 hover:text-white hover:bg-gray-800'
                    : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                }`}
                aria-label="Toggle theme"
              >
                {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
              </button>
              <button 
                onClick={clearLogs}
                className={`text-sm transition-colors duration-200 ${
                  theme === 'dark'
                    ? 'text-gray-400 hover:text-white'
                    : 'text-gray-600 hover:text-gray-900'
                }`}
              >
                Clear
              </button>
            </div>
          </div>
          <NetworkFilter currentFilter={filter} onFilterChange={setFilter} />
        </div>
      </div>

      {/* Logs List */}
      <div className="px-4 pb-4">
        {filteredLogs.length === 0 ? (
          <div className={`text-center py-12 transition-colors duration-300 ${
            theme === 'dark' ? 'text-gray-500' : 'text-gray-400'
          }`}>
            <div className="text-2xl mb-2">📡</div>
            <p>No network logs yet</p>
          </div>
        ) : (
          <div className="space-y-2">
            {filteredLogs.map((log, index) => (
              <NetworkLogItem 
                key={log.id} 
                log={log} 
                index={index}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default NetworkLogs;
