import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { Subscription } from 'rxjs';
import { Trade } from '../../models/trade.model';
import { TradeService } from '../../services/trade.service';

@Component({
  selector: 'app-trade-feed',
  standalone: true,
  imports: [CommonModule, DatePipe, DecimalPipe],
  templateUrl: './trade-feed.component.html',
  styleUrls: ['./trade-feed.component.scss']
})
export class TradeFeedComponent implements OnInit, OnDestroy {
  @Input() symbol = 'BTCUSDT';

  trades: Trade[] = [];
  private readonly MAX_TRADES = 200;
  private sub?: Subscription;

  constructor(private tradeService: TradeService) {}

  ngOnInit(): void {
    this.tradeService.getRecentTrades(this.symbol, 100).subscribe({
      next: initial => { this.trades = initial; },
      error: () => {}   // table may be empty on first start
    });

    this.sub = this.tradeService.streamTrades(this.symbol).subscribe(trade => {
      this.trades = [trade, ...this.trades].slice(0, this.MAX_TRADES);
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  isBuy(t: Trade): boolean { return t.side === 'BUY'; }
}
