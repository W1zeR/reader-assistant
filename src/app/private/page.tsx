import { Metadata } from "next";
import PrivateQuotes from "@/components/PrivateQuotes";

export const metadata: Metadata = {
  title: "Помощник читателя | Приватные цитаты",
  description: "Это страница с приватными цитатами"
};

const Private = () => {
  return (
    <>
      <PrivateQuotes />
    </>
  );
};

export default Private;
