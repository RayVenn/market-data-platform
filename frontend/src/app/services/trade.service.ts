import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Trade } from '../models/trade.model';
import { WebSocketService } from './websocket.service';

@Injectable({ providedIn: 'root' })
export class TradeService {
  constructor(private http: HttpClient, private ws: WebSocketService) {}

  getRecentTrades(symbol: string, limit = 100): Observable<Trade[]> {
    return this.http.get<Trade[]>('/api/trades', { params: { symbol, limit } });
  }

  streamTrades(symbol: string): Observable<Trade> {
    return this.ws.subscribe<Trade>(`/topic/trades/${symbol}`);
  }
}
