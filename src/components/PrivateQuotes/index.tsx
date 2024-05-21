"use client";

import SinglePrivateQuote from "./SinglePrivateQuote";
import { PlusIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";
import axios from "axios";
import Search from "@/components/Search";

const PrivateQuotes = () => {
  const { data: session } = useSession();
  const [quotes, setQuotes] = useState([]);
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + "/quotes/private",
      {
        headers: {
          Authorization: `Bearer ${session.accessToken}`
        }
      }
    )
      .then(response => {
        setQuotes(response.data.content);
      })
      .catch(error => {
        console.error(error);
      });
  });

  return (
    <section className="dark:bg-bg-color-dark bg-gray-light relative z-10 py-16 md:py-20 lg:py-28">
      <div className="container">
        <div className="py-4 flex justify-center mt-10">
          <button
            className="bg-green-500 hover:bg-green-700 dark:bg-green-700 dark:hover:bg-green-900 text-white py-2 px-4
            rounded-full text-base">
            <Link
              href="/private/create">
              <PlusIcon className="h-6 w-6 inline-block" /> Добавить новую цитату
            </Link>
          </button>
        </div>
        <Search placeholder="Поиск личных цитат по ключевому слову" />
        <div className="grid grid-cols-1 gap-x-8 gap-y-10 md:grid-cols-2 lg:grid-cols-3">
          {quotes.map((q) => (
            <SinglePrivateQuote key={q.id} quote={q} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default PrivateQuotes;
