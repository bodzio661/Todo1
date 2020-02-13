import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TodoService } from 'app/entities/todo/todo.service';
import { ITodo, Todo } from 'app/shared/model/todo.model';

describe('Service Tests', () => {
  describe('Todo Service', () => {
    let injector: TestBed;
    let service: TodoService;
    let httpMock: HttpTestingController;
    let elemDefault: ITodo;
    let expectedResult: ITodo | ITodo[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TodoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Todo(0, 'AAAAAAA', 'AAAAAAA', currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            doneDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Todo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            doneDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            doneDate: currentDate
          },
          returnedFromService
        );

        service.create(new Todo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Todo', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            username: 'BBBBBB',
            doneDate: currentDate.format(DATE_FORMAT),
            isDone: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            doneDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Todo', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            username: 'BBBBBB',
            doneDate: currentDate.format(DATE_FORMAT),
            isDone: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            doneDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Todo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
