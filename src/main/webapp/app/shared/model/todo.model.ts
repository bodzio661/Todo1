import { Moment } from 'moment';

export interface ITodo {
  id?: number;
  description?: string;
  username?: string;
  doneDate?: Moment;
  isDone?: boolean;
}

export class Todo implements ITodo {
  constructor(
    public id?: number,
    public description?: string,
    public username?: string,
    public doneDate?: Moment,
    public isDone?: boolean
  ) {
    this.isDone = this.isDone || false;
  }
}
