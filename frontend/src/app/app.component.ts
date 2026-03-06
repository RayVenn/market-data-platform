import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrderBookComponent } from './components/order-book/order-book.component';
import { TradeFeedComponent } from './components/trade-feed/trade-feed.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, OrderBookComponent, TradeFeedComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  selectedSymbol = 'BTCUSDT';

  readonly symbols = ['BTCUSDT', 'ETHUSDT'];
}
