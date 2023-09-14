export class Company {

  public editMode: boolean = false;
  public editedCompany: Company = null;

  constructor(
    public id: number,
    public name: string,
    public address: string,
    public taxIdentificationNumber: string,
    public pensionInsurance: number,
    public healthInsurance: number
  ){}
}
