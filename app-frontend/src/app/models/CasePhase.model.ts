export interface CasePhase {
    id: number;
    name: string;
    FK_Case_Type: number;
    next_phase?: number | null;
  }
  

export interface CaseType {
  id: number;
  name: string;
  description: string;
  phases?: CasePhase[];
}
