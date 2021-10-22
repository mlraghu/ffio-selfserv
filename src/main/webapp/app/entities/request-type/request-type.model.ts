export interface IRequestType {
  id?: number;
  requestTypeID?: string | null;
  requestTypeName?: string | null;
  requestTypeInput?: string;
  approvalProcess?: string;
  automationProcess?: string;
}

export class RequestType implements IRequestType {
  constructor(
    public id?: number,
    public requestTypeID?: string | null,
    public requestTypeName?: string | null,
    public requestTypeInput?: string,
    public approvalProcess?: string,
    public automationProcess?: string
  ) {}
}

export function getRequestTypeIdentifier(requestType: IRequestType): number | undefined {
  return requestType.id;
}
