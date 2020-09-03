import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFaktura, Faktura } from 'app/shared/model/faktura.model';
import { FakturaService } from './faktura.service';
import { IKontrachent } from 'app/shared/model/kontrachent.model';
import { KontrachentService } from 'app/entities/kontrachent/kontrachent.service';

@Component({
  selector: 'jhi-faktura-update',
  templateUrl: './faktura-update.component.html',
})
export class FakturaUpdateComponent implements OnInit {
  isSaving = false;
  kontrachents: IKontrachent[] = [];
  dataFakturyDp: any;

  editForm = this.fb.group({
    id: [],
    numerFaktury: [null, [Validators.required]],
    kwotaFaktury: [null, [Validators.required]],
    dataFaktury: [null, [Validators.required]],
    typFaktury: [null, [Validators.required]],
    zalegloscFaktury: [],
    statusFaktury: [],
    kontrachent: [],
  });

  constructor(
    protected fakturaService: FakturaService,
    protected kontrachentService: KontrachentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faktura }) => {
      this.updateForm(faktura);

      this.kontrachentService.query().subscribe((res: HttpResponse<IKontrachent[]>) => (this.kontrachents = res.body || []));
    });
  }

  updateForm(faktura: IFaktura): void {
    this.editForm.patchValue({
      id: faktura.id,
      numerFaktury: faktura.numerFaktury,
      kwotaFaktury: faktura.kwotaFaktury,
      dataFaktury: faktura.dataFaktury,
      typFaktury: faktura.typFaktury,
      zalegloscFaktury: faktura.zalegloscFaktury,
      statusFaktury: faktura.statusFaktury,
      kontrachent: faktura.kontrachent,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const faktura = this.createFromForm();
    if (faktura.id !== undefined) {
      this.subscribeToSaveResponse(this.fakturaService.update(faktura));
    } else {
      this.subscribeToSaveResponse(this.fakturaService.create(faktura));
    }
  }

  private createFromForm(): IFaktura {
    return {
      ...new Faktura(),
      id: this.editForm.get(['id'])!.value,
      numerFaktury: this.editForm.get(['numerFaktury'])!.value,
      kwotaFaktury: this.editForm.get(['kwotaFaktury'])!.value,
      dataFaktury: this.editForm.get(['dataFaktury'])!.value,
      typFaktury: this.editForm.get(['typFaktury'])!.value,
      zalegloscFaktury: this.editForm.get(['zalegloscFaktury'])!.value,
      statusFaktury: this.editForm.get(['statusFaktury'])!.value,
      kontrachent: this.editForm.get(['kontrachent'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFaktura>>): void {
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

  trackById(index: number, item: IKontrachent): any {
    return item.id;
  }
}
