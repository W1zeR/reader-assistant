import { Metadata } from "next";
import PendingQuotes from "@/components/PendingQuotes";

export const metadata: Metadata = {
  title: "Помощник читателя | Цитаты в ожидании опубликования",
  description: "Это страница с цитатами в ожидании опубликования"
};

export default async function Pending({ searchParams }: {
  searchParams?: {
    query?: string;
    page?: string;
    size?: string;
    sort?: string;
  };
}) {
  return (
    <>
      <PendingQuotes searchParams={searchParams}/>
    </>
  );
};
