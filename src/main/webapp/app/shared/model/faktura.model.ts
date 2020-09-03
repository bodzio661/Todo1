import { Moment } from 'moment';
import { IKontrachent } from 'app/shared/model/kontrachent.model';
import { Type } from 'app/shared/model/enumerations/type.model';

export interface IFaktura {
  id?: number;
  numerFaktury?: string;
  kwotaFaktury?: number;
  dataFaktury?: Moment;
  typFaktury?: Type;
  zalegloscFaktury?: boolean;
  statusFaktury?: boolean;
  kontrachent?: IKontrachent;
}

export class Faktura implements IFaktura {
  constructor(
    public id?: number,
    public numerFaktury?: string,
    public kwotaFaktury?: number,
    public dataFaktury?: Moment,
    public typFaktury?: Type,
    public zalegloscFaktury?: boolean,
    public statusFaktury?: boolean,
    public kontrachent?: IKontrachent
  ) {
    this.zalegloscFaktury = this.zalegloscFaktury || false;
    this.statusFaktury = this.statusFaktury || false;
  }
}
