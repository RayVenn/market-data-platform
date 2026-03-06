export interface Trade {
  tradeTime: string;
  symbol: string;
  source: string;
  tradeId: number;
  price: number;
  quantity: number;
  side: 'BUY' | 'SELL';
  latencyMs: number;
}
