import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKontrachent } from 'app/shared/model/kontrachent.model';
import { KontrachentService } from './kontrachent.service';

@Component({
  templateUrl: './kontrachent-delete-dialog.component.html',
})
export class KontrachentDeleteDialogComponent {
  kontrachent?: IKontrachent;

  constructor(
    protected kontrachentService: KontrachentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kontrachentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('kontrachentListModification');
      this.activeModal.close();
    });
  }
}
