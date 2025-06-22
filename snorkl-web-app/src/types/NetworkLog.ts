
export interface NetworkLog {
  id: string;
  type: 'http' | 'websocket';
  url: string;
  timestamp: Date;
  
  // HTTP specific fields
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  status?: number;
  duration?: number;
  size?: number;
  
  // WebSocket specific fields
  event?: 'connect' | 'disconnect' | 'message' | 'error';
  direction?: 'incoming' | 'outgoing';
  wsStatus?: 'opened' | 'closed' | 'connecting' | 'error';
  data?: string | object;
}
