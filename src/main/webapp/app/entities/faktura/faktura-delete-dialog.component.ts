import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFaktura } from 'app/shared/model/faktura.model';
import { FakturaService } from './faktura.service';

@Component({
  templateUrl: './faktura-delete-dialog.component.html',
})
export class FakturaDeleteDialogComponent {
  faktura?: IFaktura;

  constructor(protected fakturaService: FakturaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fakturaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fakturaListModification');
      this.activeModal.close();
    });
  }
}
