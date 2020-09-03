import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IKontrachent, Kontrachent } from 'app/shared/model/kontrachent.model';
import { KontrachentService } from './kontrachent.service';

@Component({
  selector: 'jhi-kontrachent-update',
  templateUrl: './kontrachent-update.component.html',
})
export class KontrachentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nazwaKontrachenta: [],
    terminKontrachenta: [],
    typKontrachenta: [],
  });

  constructor(protected kontrachentService: KontrachentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kontrachent }) => {
      this.updateForm(kontrachent);
    });
  }

  updateForm(kontrachent: IKontrachent): void {
    this.editForm.patchValue({
      id: kontrachent.id,
      nazwaKontrachenta: kontrachent.nazwaKontrachenta,
      terminKontrachenta: kontrachent.terminKontrachenta,
      typKontrachenta: kontrachent.typKontrachenta,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kontrachent = this.createFromForm();
    if (kontrachent.id !== undefined) {
      this.subscribeToSaveResponse(this.kontrachentService.update(kontrachent));
    } else {
      this.subscribeToSaveResponse(this.kontrachentService.create(kontrachent));
    }
  }

  private createFromForm(): IKontrachent {
    return {
      ...new Kontrachent(),
      id: this.editForm.get(['id'])!.value,
      nazwaKontrachenta: this.editForm.get(['nazwaKontrachenta'])!.value,
      terminKontrachenta: this.editForm.get(['terminKontrachenta'])!.value,
      typKontrachenta: this.editForm.get(['typKontrachenta'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKontrachent>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
