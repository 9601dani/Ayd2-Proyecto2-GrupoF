export interface CasePhase {
    id: number;
    name: string;
    FK_Case_Types: number;
    next_phase?: number | null;
  }
  

export interface CaseType {
  id: number;
  name: string;
  description: string;
}
