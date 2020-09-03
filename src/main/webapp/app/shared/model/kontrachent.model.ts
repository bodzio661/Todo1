import { IFaktura } from 'app/shared/model/faktura.model';

export interface IKontrachent {
  id?: number;
  nazwaKontrachenta?: string;
  terminKontrachenta?: number;
  typKontrachenta?: boolean;
  fakturas?: IFaktura[];
}

export class Kontrachent implements IKontrachent {
  constructor(
    public id?: number,
    public nazwaKontrachenta?: string,
    public terminKontrachenta?: number,
    public typKontrachenta?: boolean,
    public fakturas?: IFaktura[]
  ) {
    this.typKontrachenta = this.typKontrachenta || false;
  }
}
