import { browser, by, element } from 'protractor';

export class CompanyPage {
  async navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl);
  }

  async companyRows(): ElementArrayFinder {
          return element.all(by.css('.companyRow'))
      }

  async anyCompanyRow(): ElementFinder {
      return element(by.css('.companyRow'))
  }

  async nameHeaderValue(): Promise<string> {
      return element(by.css('#nameHeader')).getText();
    }

  async addressHeaderValue(): Promise<string> {
      return element(by.css('#addressHeader')).getText();
    }

  async taxIdHeaderValue(): Promise<string> {
      return element(by.css('#taxIdHeader')).getText();
    }

  async pensionInsHeaderValue(): Promise<string> {
      return element(by.css('#pensionInsHeader')).getText();
    }

  async healthInsHeaderValue(): Promise<string> {
      return element(by.css('#healthInsHeader')).getText();
    }

  async getTitleText(): Promise<string> {
    return element(by.css('app-root .content span')).getText();
  }

  async addNewCompany(name: string, address: string, taxId: string, pensionIns: number, healthIns: number) {
    await this.nameInput().sendKeys(name)
    await this.addressInput().sendKeys(address)
    await this.taxIdInput().sendKeys(taxId)

    await this.pensionInsInput().clear()
    await this.pensionInsInput().sendKeys(pensionIns)

    await this.healthInsInput().clear()
    await this.healthInsInput().sendKeys(healthIns)

    await element(by.id("addCompanyBtn")).click()
  }

    private nameInput() {
       return element(by.css('input[name=name]'));
     }

    private addressInput() {
       return element(by.css('input[name=address]'));
     }

    private taxIdInput() {
       return element(by.css('input[name=taxIdentificationNumber]'));
     }

    private pensionInsuranceInput() {
       return element(by.css('input[name=pensionInsurance]'));
     }

    private healthInsuranceInput() {
      return element(by.css('input[name=healthInsurance]'));
     }
}
