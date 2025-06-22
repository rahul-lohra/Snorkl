import React, { useState, useEffect } from 'react';
import type { NetworkLog } from '../types/NetworkLog';
import { useTheme } from '../contexts/ThemeContext';
import { ArrowDown, ArrowUp, ArrowUpDown, Wifi, WifiOff, Clock, CheckCircle, XCircle, AlertCircle } from 'lucide-react';

interface NetworkLogItemProps {
  log: NetworkLog;
  index: number;
}

export const NetworkLogItem: React.FC<NetworkLogItemProps> = ({ log, index }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isVisited, setIsVisited] = useState(false);
  const { theme } = useTheme();

  // Load visited state from localStorage on mount
  useEffect(() => {
    const visitedLogs = JSON.parse(localStorage.getItem('visitedNetworkLogs') || '[]');
    setIsVisited(visitedLogs.includes(log.id));
  }, [log.id]);

  // Mark as visited when expanded and save to localStorage
  const handleToggleExpanded = () => {
    const newExpandedState = !isExpanded;
    setIsExpanded(newExpandedState);
    
    if (newExpandedState && !isVisited) {
      setIsVisited(true);
      const visitedLogs = JSON.parse(localStorage.getItem('visitedNetworkLogs') || '[]');
      if (!visitedLogs.includes(log.id)) {
        visitedLogs.push(log.id);
        localStorage.setItem('visitedNetworkLogs', JSON.stringify(visitedLogs));
      }
    }
  };

  const getStatusColor = (status?: number) => {
    if (!status) return 'text-blue-400';
    if (status >= 200 && status < 300) return 'text-green-400';
    if (status >= 300 && status < 400) return 'text-yellow-400';
    if (status >= 400) return 'text-red-400';
    return 'text-gray-400';
  };

  const getMethodColor = (method?: string) => {
    switch (method) {
      case 'GET': return 'text-green-400 bg-green-400/10';
      case 'POST': return 'text-blue-400 bg-blue-400/10';
      case 'PUT': return 'text-yellow-400 bg-yellow-400/10';
      case 'DELETE': return 'text-red-400 bg-red-400/10';
      default: return 'text-gray-400 bg-gray-400/10';
    }
  };

  const getWebSocketStatusColor = (wsStatus?: string) => {
    switch (wsStatus) {
      case 'opened': return 'text-green-400 bg-green-400/10';
      case 'closed': return 'text-gray-400 bg-gray-400/10';
      case 'connecting': return 'text-yellow-400 bg-yellow-400/10';
      case 'error': return 'text-red-400 bg-red-400/10';
      default: return 'text-purple-400 bg-purple-400/10';
    }
  };

  const getDirectionIcon = (direction?: string) => {
    switch (direction) {
      case 'incoming': return <ArrowDown size={14} className="text-blue-400" />;
      case 'outgoing': return <ArrowUp size={14} className="text-green-400" />;
      default: return <ArrowUpDown size={14} className="text-purple-400" />;
    }
  };

  const getStatusIcon = (wsStatus?: string) => {
    switch (wsStatus) {
      case 'opened': return <CheckCircle size={14} className="text-green-400" />;
      case 'closed': return <XCircle size={14} className="text-gray-400" />;
      case 'connecting': return <Clock size={14} className="text-yellow-400" />;
      case 'error': return <AlertCircle size={14} className="text-red-400" />;
      default: return <Wifi size={14} className="text-purple-400" />;
    }
  };

  const formatTime = (timestamp: Date) => {
    return timestamp.toLocaleTimeString('en-US', { 
      hour12: false, 
      hour: '2-digit', 
      minute: '2-digit', 
      second: '2-digit' 
    });
  };

  const formatSize = (bytes?: number) => {
    if (!bytes) return '';
    if (bytes < 1024) return `${bytes}B`;
    if (bytes < 1048576) return `${(bytes / 1024).toFixed(1)}KB`;
    return `${(bytes / 1048576).toFixed(1)}MB`;
  };

  const formatUrl = (url: string) => {
    try {
      const urlObj = new URL(url);
      return `${urlObj.hostname}${urlObj.pathname}`;
    } catch {
      return url;
    }
  };

  return (
    <div 
      className={`rounded-lg border overflow-hidden animate-fade-in transition-all duration-200 relative ${
        theme === 'dark'
          ? 'bg-gray-800 border-gray-700 hover:bg-gray-750'
          : 'bg-white border-gray-200 hover:bg-gray-50'
      } ${isVisited ? 'opacity-75' : ''}`}
      style={{ animationDelay: `${index * 50}ms` }}
    >
      {/* Visited indicator dot */}
      <div className={`absolute top-2 right-2 w-2 h-2 rounded-full transition-all duration-200 ${
        isVisited 
          ? theme === 'dark' ? 'bg-green-400' : 'bg-green-500'
          : theme === 'dark' ? 'bg-gray-600' : 'bg-gray-300'
      }`} />

      <div 
        className="p-4 cursor-pointer"
        onClick={handleToggleExpanded}
      >
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center space-x-3">
            {log.type === 'http' ? (
              <div className={`px-2 py-1 rounded text-xs font-medium ${getMethodColor(log.method)}`}>
                {log.method}
              </div>
            ) : (
              <div className={`px-2 py-1 rounded text-xs font-medium flex items-center space-x-1 ${getWebSocketStatusColor(log.wsStatus)}`}>
                <WifiOff size={12} className="text-purple-400" />
                <span>WS</span>
              </div>
            )}

            {/* Status for HTTP / Direction + Status for WebSocket */}
            {log.type === 'http' && log.status && (
              <span className={`text-sm font-medium ${getStatusColor(log.status)}`}>
                {log.status}
              </span>
            )}

            {log.type === 'websocket' && (
              <div className="flex items-center space-x-2">
                {/* Direction Indicator */}
                {log.direction && (
                  <div className={`flex items-center space-x-1 px-2 py-1 rounded-full border transition-colors duration-300 ${
                    theme === 'dark' 
                      ? 'bg-gray-700 border-gray-600' 
                      : 'bg-gray-50 border-gray-300'
                  }`}>
                    {getDirectionIcon(log.direction)}
                    <span className={`text-xs font-medium transition-colors duration-300 ${
                      theme === 'dark' ? 'text-gray-300' : 'text-gray-600'
                    }`}>
                      {log.direction}
                    </span>
                  </div>
                )}

                {/* Status Indicator */}
                {log.wsStatus && (
                  <div className={`flex items-center space-x-1 px-2 py-1 rounded-full border transition-colors duration-300 ${
                    theme === 'dark' 
                      ? 'bg-gray-700 border-gray-600' 
                      : 'bg-gray-50 border-gray-300'
                  }`}>
                    {getStatusIcon(log.wsStatus)}
                    <span className={`text-xs font-medium transition-colors duration-300 ${
                      theme === 'dark' ? 'text-gray-300' : 'text-gray-600'
                    }`}>
                      {log.wsStatus}
                    </span>
                  </div>
                )}

                {/* Event Type */}
                {log.event && (
                  <div className={`px-2 py-1 rounded-full text-xs font-medium transition-colors duration-300 ${
                    theme === 'dark'
                      ? 'bg-purple-500/10 text-purple-400 border border-purple-500/20'
                      : 'bg-purple-100 text-purple-600 border border-purple-200'
                  }`}>
                    {log.event}
                  </div>
                )}
              </div>
            )}
          </div>

          {/* Consistent metadata positioning - timestamp, duration, size */}
          <div className={`flex items-center space-x-3 text-xs transition-colors duration-300 ${
            theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
          }`}>
            {log.duration && <span>{log.duration}ms</span>}
            {log.size && <span>{formatSize(log.size)}</span>}
            <span>{formatTime(log.timestamp)}</span>
          </div>
        </div>

        <div className={`text-sm mb-2 transition-colors duration-300 ${
          theme === 'dark' ? 'text-gray-300' : 'text-gray-700'
        }`}>
          <span className="font-mono">{formatUrl(log.url)}</span>
        </div>

        {log.data && !isExpanded && (
          <div className={`text-xs truncate transition-colors duration-300 ${
            theme === 'dark' ? 'text-gray-500' : 'text-gray-400'
          }`}>
            {typeof log.data === 'string' ? log.data : JSON.stringify(log.data)}
          </div>
        )}
      </div>

      {isExpanded && (
        <div className={`px-4 pb-4 border-t animate-fade-in transition-colors duration-300 ${
          theme === 'dark' ? 'border-gray-700' : 'border-gray-200'
        }`}>
          <div className="mt-3 space-y-3 text-xs">
            <div>
              <span className={`transition-colors duration-300 ${
                theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
              }`}>URL:</span>
              <span className={`ml-2 font-mono transition-colors duration-300 ${
                theme === 'dark' ? 'text-gray-300' : 'text-gray-700'
              }`}>{log.url}</span>
            </div>
            {log.type === 'websocket' && (
              <>
                {log.direction && (
                  <div className="flex items-center space-x-2">
                    <span className={`transition-colors duration-300 ${
                      theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                    }`}>Direction:</span>
                    <div className="flex items-center space-x-1">
                      {getDirectionIcon(log.direction)}
                      <span className={`transition-colors duration-300 ${
                        theme === 'dark' ? 'text-gray-300' : 'text-gray-700'
                      }`}>{log.direction}</span>
                    </div>
                  </div>
                )}
                {log.wsStatus && (
                  <div className="flex items-center space-x-2">
                    <span className={`transition-colors duration-300 ${
                      theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                    }`}>Status:</span>
                    <div className="flex items-center space-x-1">
                      {getStatusIcon(log.wsStatus)}
                      <span className={`transition-colors duration-300 ${
                        theme === 'dark' ? 'text-gray-300' : 'text-gray-700'
                      }`}>{log.wsStatus}</span>
                    </div>
                  </div>
                )}
                {log.event && (
                  <div>
                    <span className={`transition-colors duration-300 ${
                      theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                    }`}>Event:</span>
                    <span className={`ml-2 transition-colors duration-300 ${
                      theme === 'dark' ? 'text-gray-300' : 'text-gray-700'
                    }`}>{log.event}</span>
                  </div>
                )}
              </>
            )}
            {log.data && (
              <div>
                <span className={`transition-colors duration-300 ${
                  theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                }`}>Data:</span>
                <pre className={`ml-2 mt-1 p-3 rounded-lg text-xs overflow-x-auto transition-colors duration-300 ${
                  theme === 'dark' 
                    ? 'text-gray-300 bg-gray-900 border border-gray-700' 
                    : 'text-gray-700 bg-gray-100 border border-gray-200'
                }`}>
                  {typeof log.data === 'string' ? log.data : JSON.stringify(log.data, null, 2)}
                </pre>
              </div>
            )}
            <div>
              <span className={`transition-colors duration-300 ${
                theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
              }`}>Timestamp:</span>
              <span className={`ml-2 transition-colors duration-300 ${
                theme === 'dark' ? 'text-gray-300' : 'text-gray-700'
              }`}>{log.timestamp.toISOString()}</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
