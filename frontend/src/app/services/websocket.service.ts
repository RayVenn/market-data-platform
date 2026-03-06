import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private client: Client;
  private subjects = new Map<string, Subject<unknown>>();

  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('/ws'),
      reconnectDelay: 5000,
    });

    // Subscribe to all registered destinations once connected
    this.client.onConnect = () => {
      this.subjects.forEach((subject, destination) => {
        this.client.subscribe(destination, (msg: IMessage) => {
          subject.next(JSON.parse(msg.body));
        });
      });
    };

    this.client.activate();
  }

  subscribe<T>(destination: string): Observable<T> {
    if (!this.subjects.has(destination)) {
      const subject = new Subject<unknown>();
      this.subjects.set(destination, subject);

      // If already connected, subscribe immediately
      if (this.client.connected) {
        this.client.subscribe(destination, (msg: IMessage) => {
          subject.next(JSON.parse(msg.body));
        });
      }
    }
    return this.subjects.get(destination)!.asObservable() as Observable<T>;
  }
}
