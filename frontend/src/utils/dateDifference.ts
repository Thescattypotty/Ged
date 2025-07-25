function dateDifference(start: Date, end: Date): number {
    const startDate = new Date(start);
    const endDate = new Date(end);

    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(0, 0, 0, 0);

    const diffInMs = endDate.getTime() - startDate.getTime();
    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24));
    return diffInDays;
};

export default function dateDifferenceWithToday(end: Date): number {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return dateDifference(today, end);
};