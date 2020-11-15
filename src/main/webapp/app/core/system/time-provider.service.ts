import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TimeProviderService {
  constructor() {}

  now(): Date {
    return new Date();
  }
}
