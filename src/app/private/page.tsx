import { Metadata } from "next";
import PrivateQuotes from "@/components/PrivateQuotes";

export const metadata: Metadata = {
  title: "Помощник читателя | Приватные цитаты",
  description: "Это страница с приватными цитатами"
};

export default async function Private({ searchParams }: {
  searchParams?: {
    query?: string;
    page?: string;
    size?: string;
    sort?: string;
  };
}) {
  return (
    <>
      <PrivateQuotes searchParams={searchParams} />
    </>
  );
};
