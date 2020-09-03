import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKontrachent } from 'app/shared/model/kontrachent.model';

@Component({
  selector: 'jhi-kontrachent-detail',
  templateUrl: './kontrachent-detail.component.html',
})
export class KontrachentDetailComponent implements OnInit {
  kontrachent: IKontrachent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kontrachent }) => (this.kontrachent = kontrachent));
  }

  previousState(): void {
    window.history.back();
  }
}
