export class RegionCreateDTO {
    constructor(
        public code: string,
        public name: string
    ) { }
}

export interface RegionDTO {
    id: number;
    code: string;
    name: string;
}
