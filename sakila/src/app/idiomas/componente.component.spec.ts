import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdiomasComponent } from './componente.component';

describe('ActoresComponent', () => {
  let component: IdiomasComponent;
  let fixture: ComponentFixture<IdiomasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IdiomasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IdiomasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
