
import { Link } from 'react-router-dom';

const Index = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900">
      <div className="text-center">
        <h1 className="text-4xl font-bold mb-8 text-white">Network Monitor</h1>
        <p className="text-xl text-gray-400 mb-8">Mobile-first network logging interface</p>
        <Link 
          to="/network-logs"
          className="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          <span className="mr-2">📱</span>
          View Network Logs
        </Link>
      </div>
    </div>
  );
};

export default Index;
