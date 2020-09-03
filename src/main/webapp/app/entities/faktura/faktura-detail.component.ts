import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFaktura } from 'app/shared/model/faktura.model';

@Component({
  selector: 'jhi-faktura-detail',
  templateUrl: './faktura-detail.component.html',
})
export class FakturaDetailComponent implements OnInit {
  faktura: IFaktura | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faktura }) => (this.faktura = faktura));
  }

  previousState(): void {
    window.history.back();
  }
}
