import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface IRequest {
  id?: number;
  requestID?: number | null;
  requestType?: string | null;
  projectInfo?: string;
  costCode?: string;
  environments?: number;
  configInput?: string;
  status?: RequestStatus;
}

export class Request implements IRequest {
  constructor(
    public id?: number,
    public requestID?: number | null,
    public requestType?: string | null,
    public projectInfo?: string,
    public costCode?: string,
    public environments?: number,
    public configInput?: string,
    public status?: RequestStatus
  ) {}
}

export function getRequestIdentifier(request: IRequest): number | undefined {
  return request.id;
}
