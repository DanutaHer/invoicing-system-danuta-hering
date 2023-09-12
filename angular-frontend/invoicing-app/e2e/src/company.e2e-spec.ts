import { browser, logging } from 'protractor';
import { AppPage } from './app.po';

describe('CompanyPage E2E test', () => {
  let page: CompanyPage;

  beforeEach(() => {
    page = new CompanyPage();

    await page.navigateTo();

    await page.companyRows()
                .each(async (row) => {
                    let companyRow = new CompanyRow(row);
                    await companyRow.deleteBtn().click()
                })

            browser.wait(ExpectedConditions.not(ExpectedConditions.presenceOf(page.anyCompanyRow())));

            expect(await page.companyRows()).toEqual([])
  });

  it('should display correct values for table headers', async () => {
    await page.navigateTo();
    expect(await page.nameHeaderValue()).toEqual('Name');
    expect(await page.addressHeaderValue()).toEqual('Address');
    expect(await page.taxIdHeaderValue()).toEqual('Tax Identification Number');
    expect(await page.pensionInsHeaderValue()).toEqual('Pension Insurance');
    expect(await page.healthInsHeaderValue()).toEqual('Health Insurance');
  });

  it('can add company', async () => {
      await page.addNewCompany("Global", "ul. Zielona 3", "123-123-123", 111.11, 333.33)

      await page.companyRows().then( async rows => {
        expect(rows.length).toEqual(1);
        await new CompanyRow(rows[0]).assertRowValues("Global", "ul. Zielona 3", "123-123-123", "111.11", "333.33")
      })
  })

  it('can delete company', async () => {
      await page.addNewCompany("Global1", "ul. Zielona 1", "123-123-123", 111.11, 333.33)
      await page.addNewCompany("Global2", "ul. Zielona 2", "123-123-123", 111.11, 333.33)

      await page.companyRows().then(async rowsBeforeDelete => {
          expect(rowsBeforeDelete.length).toEqual(2);
          await new CompanyRow(rowsBeforeDelete[0]).deleteBtn().click()

          await page.companyRows().then(async rowsAfterDelete => {
              expect(rowsAfterDelete.length).toEqual(1);
              await new CompanyRow(rowsAfterDelete[0]).assertRowValues("Global2", "ul. Zielona 2", "123-123-123", "111.11", "333.33")
          });
      })
  });

  it('can update company', async () => {
      await page.addNewCompany("Global1", "ul. Zielona 1", "123-123-123", 111.11, 333.33)

      await page.companyRows().then(async rows => {
          const companyRow = new CompanyRow(rows[0]);
          await companyRow.updateCompany("Global111", "ul. Zielona 123", "123-123-123", 111.11, 333.33)
          await companyRow.assertRowValues("Global111", "ul. Zielona 123", "123-123-123", "111.11", "333.33")
      })
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
