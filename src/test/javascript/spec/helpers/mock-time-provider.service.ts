import Spy = jasmine.Spy;
import { SpyObject } from './spyobject';
import { TimeProviderService } from 'app/core/system/time-provider.service';

export const DEFAULT_NOW_TIME = new Date();

export class MockTimeProviderService extends SpyObject {
  getSpy: Spy;
  nowSpy: Spy;

  constructor() {
    super(TimeProviderService);

    this.getSpy = this.spy('get').andReturn(this);
    this.nowSpy = this.spy('now').andReturn(DEFAULT_NOW_TIME);
  }
}
