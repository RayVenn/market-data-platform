import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderBook } from '../models/orderbook.model';
import { WebSocketService } from './websocket.service';

@Injectable({ providedIn: 'root' })
export class OrderBookService {
  constructor(private http: HttpClient, private ws: WebSocketService) {}

  getOrderBook(symbol: string): Observable<OrderBook[]> {
    return this.http.get<OrderBook[]>('/api/orderbook', { params: { symbol } });
  }

  streamOrderBook(symbol: string): Observable<OrderBook> {
    return this.ws.subscribe<OrderBook>(`/topic/orderbook/${symbol}`);
  }
}
