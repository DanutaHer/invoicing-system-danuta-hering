import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { CompanyService } from './company.service';
import { Company } from './company';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: CompanyService, useClass: MockCompanyService }
      ],
      declarations: [
        AppComponent
      ],
      imports: [
        FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();
  });

  it(`should display a list of companies`, () => {
    expect(fixture.nativeElement.innerText.replace(/\s+/g, '')).toContain("Global ul. Zielona 1 123-123-123 11.11 111.11".replace(/\s+/g, ''))
    expect(fixture.nativeElement.innerText.replace(/\s+/g, '')).toContain("Global ul. Zielona 2 123-123-123 22.22 222.22".replace(/\s+/g, ''))

    expect(component.companies.length).toBe(2)
    expect(component.companies).toBe(MockCompanyService.companies)
  });

  it(`should add company to the list`, () => {
    const nameInput: HTMLInputElement = fixture.nativeElement.querySelector("input[name=name]")
    nameInput.value = "Global"
    nameInput.dispatchEvent(new Event('input'));

    const addressInput: HTMLInputElement = fixture.nativeElement.querySelector("input[name=address]")
    addressInput.value = "ul.Zielona 1"
    addressInput.dispatchEvent(new Event('input'));

    const taxIdInput: HTMLInputElement = fixture.nativeElement.querySelector("input[name=taxIdentificationNumber]")
    taxIdInput.value = "123-123-123"
    taxIdInput.dispatchEvent(new Event('input'));

    const addCompanyBtn: HTMLElement = fixture.nativeElement.querySelector("#addCompanyBtn")
    addCompanyBtn.click()

    fixture.detectChanges();

    expect(fixture.nativeElement.innerText.replace(/\s+/g, '')).toContain("Global ul.Zielona 1 123-123-123".replace(/\s+/g, ''))
  });

  class MockCompanyService {
    static companies: Company[] = [
      new Company(
        1,
        "Global",
        "ul. Zielona 1",
        "123-123-123",
        11.11,
        111.11
      ),
      new Company(
        2,
        "Global",
        "ul. Zielona 2",
        "123-123-123",
        22.22,
        222.22
      )
    ];

    getCompanies() {
      return of(MockCompanyService.companies)
    }

    addCompany(company: Company) {
      MockCompanyService.companies.push(company)
      return of()
    }
  }
});
