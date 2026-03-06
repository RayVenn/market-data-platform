import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { Subscription } from 'rxjs';
import { OrderBook, PriceLevel } from '../../models/orderbook.model';
import { OrderBookService } from '../../services/orderbook.service';

@Component({
  selector: 'app-order-book',
  standalone: true,
  imports: [CommonModule, DecimalPipe],
  templateUrl: './order-book.component.html',
  styleUrls: ['./order-book.component.scss']
})
export class OrderBookComponent implements OnInit, OnDestroy {
  @Input() symbol = 'BTCUSDT';

  orderBooks = new Map<string, OrderBook>();
  private sub?: Subscription;
  private initSub?: Subscription;

  constructor(private orderBookService: OrderBookService) {}

  ngOnInit(): void {
    this.initSub = this.orderBookService.getOrderBook(this.symbol).subscribe({
      next: books => books.forEach(b => this.orderBooks.set(b.source, b)),
      error: () => {}
    });

    this.sub = this.orderBookService.streamOrderBook(this.symbol).subscribe(book => {
      this.orderBooks.set(book.source, book);
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.initSub?.unsubscribe();
  }

  get exchanges(): string[] {
    return Array.from(this.orderBooks.keys()).sort();
  }

  topAsks(book: OrderBook, n = 10): PriceLevel[] {
    return book.asks.slice(0, n).reverse();   // lowest ask at bottom
  }

  topBids(book: OrderBook, n = 10): PriceLevel[] {
    return book.bids.slice(0, n);             // highest bid at top
  }
}
