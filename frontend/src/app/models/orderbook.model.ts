export interface PriceLevel {
  price: number;
  quantity: number;
}

export interface OrderBook {
  source: string;
  symbol: string;
  snapshotTime: string;
  bids: PriceLevel[];
  asks: PriceLevel[];
}
