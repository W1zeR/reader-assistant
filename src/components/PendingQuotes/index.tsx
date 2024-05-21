"use client";

import SinglePendingQuote from "./SinglePendingQuote";
import { useEffect, useState } from "react";
import axios from "axios";
import { useSession } from "next-auth/react";
import Search from "@/components/Search";

const PendingQuotes = () => {
  const { data: session } = useSession();
  const [quotes, setQuotes] = useState([]);
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + "/quotes/pending",
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
        <div className="mt-10">
          <Search placeholder="Поиск цитат в ожидании опубликования по ключевому слову" />
        </div>
        <div className="grid grid-cols-1 gap-x-8 gap-y-10 md:grid-cols-2 lg:grid-cols-3">
          {quotes.map((q) => (
            <SinglePendingQuote key={q.id} quote={q} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default PendingQuotes;
